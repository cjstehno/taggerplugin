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
package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.manager.TagAssociationException;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.preferences.PreferenceConstants;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 *	This action is used to delete a tag associtaion from a resource. When fired, this action will open
 *	the Tag Selection Dialog so that the associations may be selected.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class DeleteTagAction implements IViewActionDelegate {

	private IViewPart view;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see IViewActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		final TagView tagView = (TagView)view;
		final Tag[] deletedTags = tagView.getSelectedTags();
		if(deleteConfirmed(deletedTags.length)){
			if(deletedTags != null && deletedTags.length != 0){
				try {
					TagManager.getInstance().deleteTags(deletedTags);
				} catch(TagAssociationException te){
					ExceptionDialogFactory.create(view.getSite().getShell(), te).open();
				}
			}
		}
	}

	/**
	 * Used to determine (based on preferences) if the delete should be confirmed. If no tags
	 * are selected, this will always return false.
	 *
	 * @param tagCnt the number of selected tags
	 * @return a value of true if the deletion is confirmed
	 */
	private boolean deleteConfirmed(int tagCnt){
		if(tagCnt == 0){return(false);}
		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		if(store.getBoolean(PreferenceConstants.CONFIRM_DELETE_TAG.getKey())){
			return(MessageDialog.openConfirm(view.getSite().getShell(),TaggerMessages.DeleteTagAction_Confirm_Title,TaggerMessages.DeleteTagAction_Confirm_Text));
		} else {
			return(true);
		}
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
}
