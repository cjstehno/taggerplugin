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

import net.sourceforge.taggerplugin.dialog.TagDialog;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * 	This action opens the tag editor dialog when fired to allow tag data to be modified. If
 * 	the OK button is pressed, the new data in the dialog will be stored.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ModifyTagAction implements IViewActionDelegate {

	private IViewPart view;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see IViewActionDelegate#run(IAction action)
	 */
	public void run(IAction action) {
		final TagView tagView = (TagView)view;
		final Tag selectedTag = tagView.getSelectedTag();
		if(selectedTag != null){
			final TagDialog dialog = new TagDialog(view.getSite().getShell());
			dialog.setTag(selectedTag);
			if(dialog.showModify() == TagDialog.OK){
				TagManager.getInstance().updateTag(selectedTag);
			}
		}
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
}
