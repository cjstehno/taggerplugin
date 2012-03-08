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


import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.ui.IWorkingSet;

/**
 * Tag Search Query object.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchQuery implements ISearchQuery {

	private final TagSearchInput input;
	private final ISearchResult result;

	TagSearchQuery(final TagSearchInput input, final TagSearchResult result){
		super();
		this.input = input;

		this.result = result;
		result.setQuery(this);
	}

	public boolean canRerun() {return true;}

	public boolean canRunInBackground() {return true;}

	public String getLabel() {return(TaggerMessages.TagSearchQuery_Label);}

	public ISearchResult getSearchResult() {return(result);}

	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		try {
			final IResourceProxyVisitor visitor = new TaggableResourceVisitor(input.getTagIds(),input.isRequired(),(ITagSearchResult)result);

			if(input.isProjectsScope()){
				final String[] projectNames = input.getProjectNames();
				for (String projectName : projectNames) {
					final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(projectName);
					if(resource.getAdapter(IProject.class) != null){
						resource.accept(visitor,IResource.NONE);
					}
				}

			} else if(input.isWorkingSetScope()){
				final IWorkingSet[] workingSets = input.getWorkingSets();
				for (IWorkingSet workingSet : workingSets) {
					final IAdaptable[] elems = workingSet.getElements();
					for (IAdaptable adaptable : elems) {
						final IResource resource = (IResource)adaptable.getAdapter(IResource.class);
						if(resource != null){
							visitor.visit(resource.createProxy());
						}
					}
				}

			} else if(input.isSelectionScope()){
				for(IResource resource : input.getSelectedResources()){
					visitor.visit(resource.createProxy());
				}

			} else {
				// workspace scope
				ResourcesPlugin.getWorkspace().getRoot().accept(visitor, IResource.NONE);
			}

			return(new Status(IStatus.OK,TaggerActivator.PLUGIN_ID,IStatus.OK,TaggerMessages.TagSearchQuery_Status_Complete,null));
		} catch(CoreException ce){
			TaggerLog.error("Unable to perform search: " + ce.getMessage(), ce);
			return(new Status(IStatus.ERROR,TaggerActivator.PLUGIN_ID,ce.getStatus().getCode(),TaggerMessages.TagSearchQuery_Status_Error,ce));
		}
	}
}