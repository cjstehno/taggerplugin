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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Action to create a new tag and add it to the tag set. This action will open the TagDialog in create mode.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class NewTagAction implements IViewActionDelegate {

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
		final TagDialog dialog = new TagDialog(view.getSite().getShell());
		if(dialog.showCreate() == TagDialog.OK){
			TagManager.getInstance().addTag(dialog.getTag());
		}
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
}
