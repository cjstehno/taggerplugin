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

import java.util.Set;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.dialog.TagSelectionDialog;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action is used to add the same tag associations to all selected resources.
 * Tag associations that are common to all selected resources will not be available
 * for selection; however, associations common to only some of the resources will
 * be available.
 *
 * This action will display the TagSelectionDialog
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class AddTagAssociationAction implements IObjectActionDelegate {

	private IWorkbenchPart activePart;
	private ISelection selection;

	/**
	 * @see IObjectActionDelegate#run(IAction action)
	 */
	public void run(IAction action) {
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				try {
					final ITaggable[] taggables = extractTaggables(sel);

					final Set<String> sharedAssociations = TagAssociationManager.getInstance().findSharedAssociations(taggables);
					final Tag[] taglist = TagManager.getInstance().findTagsNotIn(sharedAssociations.toArray(new String[sharedAssociations.size()]));

					final TagSelectionDialog dialog = new TagSelectionDialog(activePart.getSite().getShell(),taglist,TaggerMessages.AddTagAssociationAction_Title,TaggerMessages.AddTagAssociationAction_Message);
					if(dialog.open() == TagSelectionDialog.OK){
						final Object[] selectedTags = dialog.getResult();
						for (Object tag : selectedTags) {
							final Tag t = (Tag)tag;
							for(ITaggable taggable : taggables){
								taggable.setTag(t.getId());
							}
						}
					}
				} catch(Exception ex){
					ExceptionDialogFactory.create(activePart.getSite().getShell(), ex).open();
				}
			}
		}
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.activePart = targetPart;
	}

	/**
	 * @see IObjectActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	/**
	 * Used to extract the taggable items from the selection.
	 *
	 * @param sel the selection
	 * @return the taggable items selected
	 */
	private ITaggable[] extractTaggables(IStructuredSelection sel){
		final Object[] resources = sel.toArray();
		final ITaggable[] taggables = new ITaggable[resources.length];
		for(int i=0; i<resources.length; i++){
			taggables[i] = (ITaggable)((IResource)(resources[i])).getAdapter(ITaggable.class);
		}
		return(taggables);
	}
}
