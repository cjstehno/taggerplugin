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

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

/**
 * Action to create a new working set from the resources in the search results view.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class CreateWorkingSetFromResultsAction implements IViewActionDelegate {

	private IViewPart viewPart;
	
	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.viewPart = view;
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
	
	/**
	 * @see IViewActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		final ISelection selection = viewPart.getSite().getSelectionProvider().getSelection();
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final IResource[] resources = new IResource[sel.size()];
				int i = 0;
				for(Object obj : sel.toArray()){
					resources[i++] = (IResource)obj;
				}

				createWorkingSet(resources);
			}
		}
	}

	/**
	 * Creates a new working set from the given resources.
	 *
	 * @param resources the resources to be added to the working set
	 */
	private void createWorkingSet(final IResource[] resources){
		// TODO: merge this with the other CreateWorkingSet action's ws create method to share duty
		final IWorkingSetManager workingSetMgr = PlatformUI.getWorkbench().getWorkingSetManager();

		final IWorkingSet workingSet = workingSetMgr.createWorkingSet(TaggerMessages.bind(TaggerMessages.CreateWorkingSetFromResultsAction_Name, createDateStamp()),resources);
		workingSetMgr.addWorkingSet(workingSet);

		MessageDialog.openInformation(
			viewPart.getSite().getShell(),
			TaggerMessages.CreateWorkingSetFromResultsAction_Dialog_Title,
			TaggerMessages.bind(TaggerMessages.CreateWorkingSetFromResultsAction_Dialog_Text,workingSet.getName())
		);
	}
	
	/**
	 * Creates a date stamp string for the working set title.
	 *
	 * @return a string representation of the current date and time
	 */
	private String createDateStamp(){
		return(new SimpleDateFormat("M/d/yyyy HH:mm").format(new Date()));
	}
}
