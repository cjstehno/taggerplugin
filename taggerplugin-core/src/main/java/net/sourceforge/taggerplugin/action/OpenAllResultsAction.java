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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.OpenFileAction;

/**
 * Action used to open all search results (resources, not projects or folders) in their appropriate editors.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class OpenAllResultsAction implements IViewActionDelegate {

	private IViewPart viewPart;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.viewPart = view;
	}

	/**
	 * @see IViewActionDelegate#run(IAction)
	 */
	@SuppressWarnings("unchecked")
	public void run(IAction action) {
		final ISelection selection = viewPart.getSite().getSelectionProvider().getSelection();
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final List elements = new ArrayList(sel.size());
				for(Object obj : sel.toArray()){
					if(!(obj instanceof IProject || obj instanceof IFolder)){
						elements.add(obj);
					}
				}
				
				final StructuredSelection selAdapter = new StructuredSelection(elements);
				if(!selAdapter.isEmpty()){
					final OpenFileAction openAction = new OpenFileAction(viewPart.getSite().getWorkbenchWindow().getActivePage());
					openAction.selectionChanged(selAdapter);
					openAction.run();	
				}
			}
		}
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
}
