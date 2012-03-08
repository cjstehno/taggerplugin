/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
package net.sourceforge.taggerplugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IDecoration;

import net.sourceforge.taggerplugin.TaggerActivator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey(),true);
		store.setDefault(PreferenceConstants.CONFIRM_DELETE_TAG.getKey(),true);
		store.setDefault(PreferenceConstants.POSITION_LABEL_DECORATION.getKey(),String.valueOf(IDecoration.TOP_RIGHT));
		store.setDefault(PreferenceConstants.TAGSET_FILE_LOCATION.getKey(),TaggerActivator.getDefault().getStateLocation().append("tags.xml").toFile().toString());
	}
}
