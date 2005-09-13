/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.ui.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.launcher.AbstractLauncherTab;
import org.eclipse.pde.internal.ui.launcher.ConfigurationAreaBlock;
import org.eclipse.pde.internal.ui.launcher.ConfigurationTemplateBlock;
import org.eclipse.pde.internal.ui.launcher.ILauncherSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

public class ConfigurationTab extends AbstractLauncherTab implements ILauncherSettings {
	
	private ConfigurationAreaBlock fConfigurationArea;
	private ConfigurationTemplateBlock fTemplateArea;
	private Image fImage;
	private boolean fJUnitConfig;
	
	public ConfigurationTab() {
		this(false);
	}
	
	public ConfigurationTab(boolean isJUnitConfig) {
		fImage = PDEPluginImages.DESC_PLUGIN_CONFIG_OBJ.createImage();
		fConfigurationArea = new ConfigurationAreaBlock(this);
		fTemplateArea = new ConfigurationTemplateBlock(this);
		fJUnitConfig = isJUnitConfig;
	}
	/*
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createStartingSpace(container, 1);
		fConfigurationArea.createControl(container);
		createStartingSpace(container, 1);
		fTemplateArea.createControl(container);
		
		Dialog.applyDialogFont(container);
		setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IHelpContextIds.LAUNCHER_CONFIGURATION);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		fConfigurationArea.setDefaults(configuration, fJUnitConfig);
		fTemplateArea.setDefaults(configuration);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			fConfigurationArea.initializeFrom(configuration);
			fTemplateArea.initializeFrom(configuration);
		} catch (CoreException e) {
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		fConfigurationArea.performApply(configuration);
		fTemplateArea.performApply(configuration);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return PDEUIMessages.ConfigurationTab_name; 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return fImage;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#dispose()
	 */
	public void dispose() {
		if (fImage != null)
			fImage.dispose();
	}

	public void validatePage() {
		String error = fConfigurationArea.validate();
		if (error == null)
			error = fTemplateArea.validate();
		setErrorMessage(error);
	}
}
