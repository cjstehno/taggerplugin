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
package net.sourceforge.taggerplugin.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.ui.IWorkingSet;

/**
 * Tag Search page input object. Used to store the input values from the UI form.
 * This object also stores the search scope-based input added by the search container.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchInput {

	private final String[] tagIds;
	private final boolean required;
	private final int scope;
	private String[] projectNames;
	private IWorkingSet[] workingSets;
	private IResource[] selectedResources;

	TagSearchInput(String[] tagIds, boolean required, final ISearchPageContainer container){
		super();
		this.tagIds = tagIds;
		this.required = required;

		this.scope = container.getSelectedScope();
		if(scope == ISearchPageContainer.SELECTED_PROJECTS_SCOPE){
			this.projectNames = container.getSelectedProjectNames();
		} else if(scope == ISearchPageContainer.WORKING_SET_SCOPE){
			this.workingSets = container.getSelectedWorkingSets();
		} else if(scope == ISearchPageContainer.SELECTION_SCOPE){
			final ISelection selection = container.getSelection();
			if(selection instanceof IStructuredSelection){
				final IStructuredSelection iss = (IStructuredSelection)selection;
				this.selectedResources = new IResource[iss.size()];
				int i = 0;
				for(Object obj : iss.toArray()){
					selectedResources[i++] = (IResource)obj;
				}
			}
		} else {
			// workspace scope -- nothing special
		}
	}

	public boolean isProjectsScope(){return(scope == ISearchPageContainer.SELECTED_PROJECTS_SCOPE);}

	public boolean isWorkingSetScope(){return(scope == ISearchPageContainer.WORKING_SET_SCOPE);}

	public boolean isSelectionScope(){return(scope == ISearchPageContainer.SELECTION_SCOPE);}

	public boolean isWorkspaceScope(){return(scope == ISearchPageContainer.WORKSPACE_SCOPE);}

	public String[] getProjectNames() {
		return this.projectNames;
	}

	public IWorkingSet[] getWorkingSets() {
		return this.workingSets;
	}

	public IResource[] getSelectedResources() {
		return this.selectedResources;
	}

	public String[] getTagIds() {
		return this.tagIds;
	}

	public boolean isRequired() {
		return this.required;
	}
}