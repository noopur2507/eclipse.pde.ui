/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.core;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.eclipse.core.boot.BootLoader;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.pde.core.*;
import org.eclipse.pde.core.plugin.*;
import org.eclipse.pde.internal.core.ifeature.*;
import org.eclipse.pde.internal.core.schema.SchemaRegistry;

public class PDECore extends Plugin implements IEnvironmentVariables {
	public static final String PLUGIN_ID = "org.eclipse.pde.core";
	public static final String ARG_PDELAUNCH = "-pdelaunch";
	public static final String BINARY_PROJECT_VALUE = "binary";
	public static final String BINARY_REPOSITORY_PROVIDER =
		PLUGIN_ID + "." + "BinaryRepositoryProvider";

	public static final String CLASSPATH_CONTAINER_ID =
		PLUGIN_ID + ".requiredPlugins";

	public static final String ECLIPSE_HOME_VARIABLE = "ECLIPSE_HOME";
	public static final QualifiedName EXTERNAL_PROJECT_PROPERTY =
		new QualifiedName(PLUGIN_ID, "imported");
	public static final String EXTERNAL_PROJECT_VALUE = "external";

	// Shared instance
	private static PDECore inst;
	public static final String SITEBUILD_DIR = ".sitebuild";
	public static final String SITEBUILD_PROPERTIES = "sitebuild.xml";
	public static final String SITEBUILD_FILE =
		SITEBUILD_DIR + "/" + SITEBUILD_PROPERTIES;
	public static final String SITEBUILD_LOG = "build.log";
	public static final String SITEBUILD_SCRIPTS = "scripts";
	public static final String SITEBUILD_TEMP_FOLDER = "temp.folder";

	public static boolean compare(
		String id1,
		String version1,
		String id2,
		String version2,
		int match) {
		if (!(id1.equals(id2)))
			return false;
		if (version1 == null)
			return true;
		if (version2 == null)
			return false;
		PluginVersionIdentifier pid1 = null;
		PluginVersionIdentifier pid2 = null;

		try {
			pid1 = new PluginVersionIdentifier(version1);
			pid2 = new PluginVersionIdentifier(version2);
		} catch (RuntimeException e) {
			// something is wrong with either - try direct comparison
			return version2.equals(version1);
		}

		switch (match) {
			case IMatchRules.NONE :
			case IMatchRules.COMPATIBLE :
				if (pid2.isCompatibleWith(pid1))
					return true;
				break;
			case IMatchRules.EQUIVALENT :
				if (pid2.isEquivalentTo(pid1))
					return true;
				break;
			case IMatchRules.PERFECT :
				if (pid2.isPerfect(pid1))
					return true;
				break;
			case IMatchRules.GREATER_OR_EQUAL :
				if (pid2.isGreaterOrEqualTo(pid1))
					return true;
				break;
		}
		return false;
	}

	public static PDECore getDefault() {
		return inst;
	}
	public static String getFormattedMessage(String key, String arg) {
		String text = getResourceString(key);
		return java.text.MessageFormat.format(text, new Object[] { arg });
	}
	public static String getFormattedMessage(String key, String[] args) {
		String text = getResourceString(key);
		return java.text.MessageFormat.format(text, args);
	}
	static IPath getInstallLocation() {
		return new Path(inst.getDescriptor().getInstallURL().getFile());
	}
	public static String getPluginId() {
		return inst.getDescriptor().getUniqueIdentifier();
	}
	public static String getResourceString(String key) {
		ResourceBundle bundle = inst.getResourceBundle();
		if (bundle != null) {
			try {
				String bundleString = bundle.getString(key);
				//return "$"+bundleString;
				return bundleString;
			} catch (MissingResourceException e) {
				// default actions is to return key, which is OK
			}
		}
		return key;
	}
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static boolean inLaunchedInstance() {
		return getDefault().isLaunchedInstance();
	}

	public static void log(IStatus status) {
		ResourcesPlugin.getPlugin().getLog().log(status);
	}

	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException)
			e = ((InvocationTargetException) e).getTargetException();
		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else
			status =
				new Status(
					IStatus.ERROR,
					getPluginId(),
					IStatus.OK,
					e.getMessage(),
					e);
		log(status);
	}

	public static void logErrorMessage(String message) {
		log(
			new Status(
				IStatus.ERROR,
				getPluginId(),
				IStatus.ERROR,
				message,
				null));
	}

	public static void logException(Throwable e) {
		logException(e, null);
	}

	public static void logException(Throwable e, String message) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else {
			if (message == null)
				message = e.getMessage();
			if (message == null)
				message = e.toString();
			status =
				new Status(
					IStatus.ERROR,
					getPluginId(),
					IStatus.OK,
					message,
					e);
		}
		ResourcesPlugin.getPlugin().getLog().log(status);
	}
	// External model manager
	private IAlternativeRuntimeSupport runtimeSupport;
	private boolean launchedInstance = false;
	private PluginModelManager modelManager;
	private boolean modelsLocked = false;
	// Resource bundle
	private ResourceBundle resourceBundle;
	// Schema registry
	private SchemaRegistry schemaRegistry;

	// User-defined attachments manager
	private SourceAttachmentManager sourceAttachmentManager;
	private SourceLocationManager sourceLocationManager;
	private TempFileManager tempFileManager;
	// Tracing options manager
	private TracingOptionsManager tracingOptionsManager;

	public PDECore(IPluginDescriptor descriptor) {
		super(descriptor);
		inst = this;
		try {
			resourceBundle =
				ResourceBundle.getBundle(
					"org.eclipse.pde.internal.core.pderesources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		initializeLaunchedInstanceFlag();
	}

	public IPluginExtensionPoint findExtensionPoint(String fullID) {
		if (fullID == null || fullID.length() == 0)
			return null;
		// separate plugin ID first
		int lastDot = fullID.lastIndexOf('.');
		if (lastDot == -1)
			return null;
		String pluginID = fullID.substring(0, lastDot);
		IPlugin plugin = findPlugin(pluginID);
		if (plugin == null)
			return null;
		String pointID = fullID.substring(lastDot + 1);
		IPluginExtensionPoint[] points = plugin.getExtensionPoints();
		for (int i = 0; i < points.length; i++) {
			IPluginExtensionPoint point = points[i];
			if (point.getId().equals(pointID))
				return point;
		}
		return null;
	}

	private IFeature findFeature(
		IFeatureModel[] models,
		String id,
		String version,
		int match) {

		if (modelsLocked)
			return null;

		for (int i = 0; i < models.length; i++) {
			IFeatureModel model = models[i];

			IFeature feature = model.getFeature();
			String pid = feature.getId();
			String pversion = feature.getVersion();
			if (compare(id, version, pid, pversion, match))
				return feature;
		}
		return null;
	}

	public IFeature findFeature(String id) {
		return findFeature(id, null, IMatchRules.NONE);
	}

	public IFeature findFeature(String id, String version, int match) {
		if (modelsLocked)
			return null;
		IWorkspaceModelManager manager = getWorkspaceModelManager();
		return findFeature(manager.getFeatureModels(), id, version, match);
	}

	public IFragment[] findFragmentsFor(String id, String version) {
		if (modelsLocked)
			return new IFragment[0];
		HashMap result = new HashMap();
		IFragment[] extFragments =
			getExternalModelManager().getFragmentsFor(id, version);
		for (int i = 0; i < extFragments.length; i++) {
			if (extFragments[i].getPluginModel().isEnabled())
				result.put(extFragments[i].getId(), extFragments[i]);
		}

		IFragment[] wFragments =
			getWorkspaceModelManager().getFragmentsFor(id, version);
		for (int i = 0; i < wFragments.length; i++) {
			result.put(wFragments[i].getId(), wFragments[i]);
		}
		return (IFragment[]) result.values().toArray(
			new IFragment[result.size()]);
	}

	public IPlugin findPlugin(String id) {
		return findPlugin(id, null, IMatchRules.NONE);
	}

	public IPlugin findPlugin(String id, String version, int match) {
		if (modelsLocked)
			return null;
		IPluginModelBase model =
			getModelManager().findPlugin(id, version, match);
		if (model != null
			&& model.isEnabled()
			&& model.getPluginBase() instanceof IPlugin)
			return (IPlugin) model.getPluginBase();
		return null;
	}

	public IExternalModelManager getExternalModelManager() {
		if (runtimeSupport == null)
			initializeModels();
		return runtimeSupport.getExternalModelManager();
	}
	public PluginModelManager getModelManager() {
		if (modelManager == null)
			initializeModels();
		return modelManager;
	}
	
	public IAlternativeRuntimeSupport getRuntimeSupport() {
		if (runtimeSupport ==null)
			initializeModels();
		return runtimeSupport;
	}
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	public SchemaRegistry getSchemaRegistry() {
		if (schemaRegistry == null)
			schemaRegistry = new SchemaRegistry();
		return schemaRegistry;
	}

	public SourceAttachmentManager getSourceAttachmentManager() {
		if (sourceAttachmentManager == null)
			sourceAttachmentManager = new SourceAttachmentManager();
		return sourceAttachmentManager;
	}
	public SourceLocationManager getSourceLocationManager() {
		if (sourceLocationManager == null)
			sourceLocationManager = new SourceLocationManager();
		return sourceLocationManager;
	}

	public TempFileManager getTempFileManager() {
		if (tempFileManager == null)
			tempFileManager = new TempFileManager();
		return tempFileManager;
	}
	public TracingOptionsManager getTracingOptionsManager() {
		if (tracingOptionsManager == null)
			tracingOptionsManager = new TracingOptionsManager();
		return tracingOptionsManager;
	}
	public IWorkspaceModelManager getWorkspaceModelManager() {
		if (runtimeSupport == null)
			initializeModels();
		return runtimeSupport.getWorkspaceModelManager();
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#initializeDefaultPluginPreferences()
	 */
	protected void initializeDefaultPluginPreferences() {
		Preferences preferences = getPluginPreferences();
		preferences.setDefault(
			ICoreConstants.TARGET_MODE,
			inLaunchedInstance()
				? ICoreConstants.VALUE_USE_OTHER
				: ICoreConstants.VALUE_USE_THIS);
		preferences.setDefault(
			ICoreConstants.CHECKED_PLUGINS,
			ICoreConstants.VALUE_SAVED_NONE);
		if (preferences
			.getString(ICoreConstants.TARGET_MODE)
			.equals(ICoreConstants.VALUE_USE_THIS))
			preferences.setValue(
				ICoreConstants.PLATFORM_PATH,
				ExternalModelManager.computeDefaultPlatformPath());
		else
			preferences.setDefault(
				ICoreConstants.PLATFORM_PATH,
				ExternalModelManager.computeDefaultPlatformPath());

		// set defaults for the target environment variables.
		preferences.setDefault(OS, BootLoader.getOS());
		preferences.setDefault(WS, BootLoader.getWS());
		preferences.setDefault(NL, Locale.getDefault().toString());
		preferences.setDefault(ARCH, BootLoader.getOSArch());
	}

	public static boolean isAlternativeRuntimeSupportEnabled() {
		Preferences pref = getDefault().getPluginPreferences();
		return pref.getBoolean(ICoreConstants.ENABLE_ALT_RUNTIME);
	}
	
	public void resetAlternativeRuntimeSupport() {
		if (modelManager != null) {
			modelManager.shutdown();
			modelManager = null;
		}
		if (runtimeSupport != null) {
			runtimeSupport.shutdown();
			runtimeSupport = null;
		}
	}

	private void initializeLaunchedInstanceFlag() {
		String[] args = BootLoader.getCommandLineArgs();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals(ARG_PDELAUNCH)) {
				launchedInstance = true;
				break;
			}
		}
	}

	private void initializeModels() {
		modelsLocked = true;
		if (runtimeSupport == null)
			loadRuntimeSupport();
		IExternalModelManager exManager =
			runtimeSupport.getExternalModelManager();
		IWorkspaceModelManager wsManager =
			runtimeSupport.getWorkspaceModelManager();

		if (modelManager == null) {
			modelManager = new PluginModelManager();
			modelManager.connect(wsManager, exManager);
		}
		modelsLocked = false;
	}

	private boolean isLaunchedInstance() {
		return launchedInstance;
	}

	public void shutdown() throws CoreException {
		PDECore.getDefault().savePluginPreferences();
		if (schemaRegistry != null) {
			schemaRegistry.shutdown();
			schemaRegistry = null;
		}
		resetAlternativeRuntimeSupport();
		if (tempFileManager != null) {
			tempFileManager.shutdown();
			tempFileManager = null;
		}
		clearMetaData();
		super.shutdown();
	}
	
	private void clearMetaData() {
		File file = getStateLocation().toFile();
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					deleteFile(files[i]);
			}
		}
	}
	
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
		}
		file.delete();
	}

	private void loadRuntimeSupport() {
		IConfigurationElement[] runtimes =
			Platform.getPluginRegistry().getConfigurationElementsFor(
				PDECore.PLUGIN_ID,
				"alternativeRuntimeSupport");

		if (runtimes.length == 0)
			return;
		IConfigurationElement runtime = runtimes[0];
		if (runtimes.length > 1) {
			// pick the first runtime support that is
			// not the default
			runtime = null;
			for (int i = 0; i < runtimes.length; i++) {
				String def = runtimes[i].getAttribute("default");
				if (def != null && def.equalsIgnoreCase("true")) {
					if (isAlternativeRuntimeSupportEnabled())
						continue;
					else {
						runtime = runtimes[i];
						break;
					}
				}
				if (runtime == null) {
					runtime = runtimes[i];
					break;
				}
			}
		}
		if (runtime == null)
			return;
		try {
			runtimeSupport =
				(IAlternativeRuntimeSupport) runtime.createExecutableExtension(
					"class");
		} catch (CoreException e) {
			logException(e);
		}
	}
}
