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
package org.eclipse.pde.internal.core.osgi.bundle;

import java.io.*;
import java.util.*;
import java.util.jar.*;

import org.eclipse.pde.core.*;
import org.eclipse.pde.core.osgi.bundle.*;
import org.eclipse.pde.internal.core.*;
import org.osgi.framework.*;

public abstract class BundleModel
	extends AbstractModel
	implements IBundleModel {
	private Dictionary fManifest;

	public Dictionary getManifest() {
		if (isLoaded() == false)
			load();
		return fManifest;
	}

	public String getInstallLocation() {
		return null;
	}

	public abstract void load();

	public boolean isFragmentModel() {
		Dictionary manifest = getManifest();
		return (manifest != null && manifest.get(Constants.FRAGMENT_HOST) != null);
	}

	public void load(InputStream source, boolean outOfSync) {
		try {
			fManifest = null;
			Manifest m = new Manifest(source);
			fManifest = manifestToProperties(m.getMainAttributes());
			if (!outOfSync)
				updateTimeStamp();
		} catch (IOException e) {
		} finally {
		}
	}
	
	private Properties manifestToProperties(Attributes d) {
		Iterator iter = d.keySet().iterator();
		Properties result = new Properties();
		while (iter.hasNext()) {
			Attributes.Name key = (Attributes.Name) iter.next();
			result.put(key.toString(), d.get(key));
		}
		return result;
	}


	public void reload(InputStream source, boolean outOfSync) {
		load(source, outOfSync);
		fireModelChanged(
			new ModelChangedEvent(this,
				IModelChangedEvent.WORLD_CHANGED,
				new Object[0],
				null));
	}
	/* (non-Javadoc)
	 * @see org.eclipse.pde.core.IModel#isReconcilingModel()
	 */
	public boolean isReconcilingModel() {
		return false;
	}
}
