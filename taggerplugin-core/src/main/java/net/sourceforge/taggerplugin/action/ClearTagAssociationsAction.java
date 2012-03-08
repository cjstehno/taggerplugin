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
import net.sourceforge.taggerplugin.preferences.PreferenceConstants;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action is used to clear all tag associations from all selected files.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ClearTagAssociationsAction implements IObjectActionDelegate {

	private IWorkbenchPart workbenchPart;
	private ISelection selection;

	/**
	 * @see IObjectActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final Object[] selectedObjs = sel.toArray();
				if(deleteConfirmed(selectedObjs.length)){
					try {
						for (Object obj : selectedObjs) {
							final IResource resource = (IResource)obj;
							final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
							taggable.clearTags();
						}
					} catch(TagAssociationException tae){
						ExceptionDialogFactory.create(workbenchPart.getSite().getShell(), tae).open();
					}
				}
			}
		}
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart){
		this.workbenchPart = targetPart;
	}

	/**
	 * @see IObjectActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	/**
	 * Used to determine if the delete is to be performed, based on number of associations 
	 * selected and the result of the confirmation dialog (if enabled in preferences).
	 *
	 * @param assocCnt the number of associations selected
	 * @return a value of true if the delete is to be performed
	 */
	private boolean deleteConfirmed(int assocCnt){
		if(assocCnt == 0){return(false);}

		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		if(store.getBoolean(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey())){
			return(MessageDialog.openConfirm(workbenchPart.getSite().getShell(),TaggerMessages.ClearTagAssociationsAction_Confirm_Title,TaggerMessages.ClearTagAssociationsAction_Confirm_Message));
		} else {
			return(true);
		}
	}
}
