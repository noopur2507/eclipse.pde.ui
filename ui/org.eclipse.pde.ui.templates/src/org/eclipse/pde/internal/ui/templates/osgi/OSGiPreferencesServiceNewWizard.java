/*******************************************************************************
 *  Copyright (c) 2006, 2007 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.ui.templates.osgi;

import org.eclipse.pde.internal.ui.templates.PDETemplateMessages;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

public class OSGiPreferencesServiceNewWizard extends NewPluginTemplateWizard {

	@Override
	public void init(IFieldData data) {
		super.init(data);
		setWindowTitle(PDETemplateMessages.OSGiPreferencesServiceNewWizard_title);
	}

	@Override
	public ITemplateSection[] createTemplateSections() {
		return new ITemplateSection[] {new OSGiPreferencesServiceTemplate()};
	}

	@Override
	public String[] getImportPackages() {
		return new String[] {"org.osgi.service.prefs;version=\"1.1\"", //$NON-NLS-1$
				"org.osgi.util.tracker;version=\"1.3.1\""}; //$NON-NLS-1$
	}

}
