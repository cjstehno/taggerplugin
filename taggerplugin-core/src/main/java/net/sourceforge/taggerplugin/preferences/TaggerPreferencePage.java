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

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TaggerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public TaggerPreferencePage() {
		super(GRID);
		setPreferenceStore(TaggerActivator.getDefault().getPreferenceStore());
		setDescription(TaggerMessages.TaggerPreferencePage_Description);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey(),TaggerMessages.TaggerPreferencePage_Label_ConfirmClear,getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.CONFIRM_DELETE_TAG.getKey(),TaggerMessages.TaggerPreferencePage_Label_ConfirmDelete,getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
			PreferenceConstants.POSITION_LABEL_DECORATION.getKey(),
			TaggerMessages.TaggerPreferencePage_Label_LabelDecoration,
			1,
			new String[][]{
				{TaggerMessages.TaggerPreferencePage_Label_LabelDecoration_TopRight,String.valueOf(IDecoration.TOP_RIGHT)},
				{TaggerMessages.TaggerPreferencePage_Label_LabelDecoration_TopLeft,String.valueOf(IDecoration.TOP_LEFT)},
				{TaggerMessages.TaggerPreferencePage_Label_LabelDecoration_BottomRight,String.valueOf(IDecoration.BOTTOM_RIGHT)},
				{TaggerMessages.TaggerPreferencePage_Label_LabelDecoration_BottomLeft,String.valueOf(IDecoration.BOTTOM_LEFT)}
			},
			getFieldEditorParent()
		));
		
//		 FIXME: externalize
		addField(new FileFieldEditor(PreferenceConstants.TAGSET_FILE_LOCATION.getKey(),"TagSet Storage File:",	true,getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {}
}