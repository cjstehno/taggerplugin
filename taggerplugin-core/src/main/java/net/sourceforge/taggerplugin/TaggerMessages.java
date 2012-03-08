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
package net.sourceforge.taggerplugin;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings accessor for the Resource Tagger plug-in.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerMessages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.messages";

	public static String DeleteTagAction_Confirm_Title;
	public static String DeleteTagAction_Confirm_Text;
	
	public static String ExceptionDetailsDialog_Label_Provider;
	public static String ExceptionDetailsDialog_Label_PluginName;
	public static String ExceptionDetailsDialog_Label_PluginId;
	public static String ExceptionDetailsDialog_Label_Version;
			
	public static String TagDialog_Title_Create;
	public static String TagDialog_Title_Modify;
	public static String TagDialog_Label_Name;
	public static String TagDialog_Label_Description;
	public static String TagDialog_Error_NoName;
	public static String TagDialog_Error_NoDescription;

	public static String AddTagAssociationAction_Title;
	public static String AddTagAssociationAction_Message;

	public static String RemoveTagAssociationAction_Title;
	public static String RemoveTagAssociationAction_Message;
	
	public static String FilterUsingTagsAction_Title;
	public static String FilterUsingTagsAction_Message;
	
	public static String TaggableResourcePropertyPage_NoAssociations;
	public static String TaggableResourcePropertyPage_Label_Associations;

	public static String TagSearchPage_Label_TagList;
	public static String TagSearchPage_Label_AllRequired;
	public static String TagSearchPage_Error_Title;
	public static String TagSearchPage_Error_Message;

	public static String TagSearchQuery_Label;
	public static String TagSearchQuery_Status_Complete;
	public static String TagSearchQuery_Status_Error;

	public static String TagSearchResult_Label;
	public static String TagSearchResult_Tooltip;

	public static String TagSearchResultPage_Label;

	public static String TagSearchResultPage_Column_Name;
	public static String TagSearchResultPage_Column_Path;
	public static String TagSearchResultPage_Column_Tags;

	public static String TagView_Header_Name;
	public static String TagView_Header_Description;

	public static String TagExportWizardPage_Title;
	public static String TagExportWizardPage_Tooltip_File;

	public static String TagImportWizardPage_Title;
	public static String TagImportWizardPage_Tooltip_File;

	public static String TagIoWizardPage_Button_Browse;
	public static String TagIoWizardPage_Label_File;
	public static String TagIoWizardPage_Label_Format;
	public static String TagIoWizardPage_Tooltip_Format;
	public static String TagIoWizardPage_Format_Xml;
	public static String TagIoWizardPage_Format_Csv;
	public static String TagIoWizardPage_Error_NoFile;

	public static String TagIo_Reading;
	public static String TagIo_Writing;
	
	public static String CreateWorkingSetFromTagsAction_Dialog_Title;
	public static String CreateWorkingSetFromTagsAction_Dialog_Text;	
	public static String CreateWorkingSetFromTagsAction_Error_Title;
	public static String CreateWorkingSetFromTagsAction_Error_Text;
	public static String CreateWorkingSetFromTagsAction_Name;
	
	public static String CreateWorkingSetFromResultsAction_Dialog_Title;
	public static String CreateWorkingSetFromResultsAction_Dialog_Text;
	public static String CreateWorkingSetFromResultsAction_Name;
	
	public static String ClearTagAssociationsAction_Confirm_Title;
	public static String ClearTagAssociationsAction_Confirm_Message;	
	
	public static String TaggerPreferencePage_Description;
	public static String TaggerPreferencePage_Label_ConfirmClear;
	public static String TaggerPreferencePage_Label_ConfirmDelete;
	public static String TaggerPreferencePage_Label_LabelDecoration;
	public static String TaggerPreferencePage_Label_LabelDecoration_TopLeft;
	public static String TaggerPreferencePage_Label_LabelDecoration_TopRight;
	public static String TaggerPreferencePage_Label_LabelDecoration_BottomLeft;
	public static String TaggerPreferencePage_Label_LabelDecoration_BottomRight;	

	public static String ExceptionDialogFactory_Title;
	
	public static String TagAssociationManager_Error_Create;
	public static String TagAssociationManager_Error_Remove;
	public static String TagAssociationManager_Error_Has;
	public static String TagAssociationManager_Error_List;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, TaggerMessages.class);
	}

	private TaggerMessages(){super();}
}
