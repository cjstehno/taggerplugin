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
package net.sourceforge.taggerplugin.jdt.action;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.action.TagAssociationFilter;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * View filter used to filter the JDT Package View.
 * 
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class PackageTagAssociationFilter extends TagAssociationFilter {

	/**
	 * @see ViewerFilter#select(Viewer, Object, Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		final IAdaptable adaptable = (IAdaptable)element;
		
		// closed projects fail fast
		if(isClosedProject(adaptable)) return(false);
		
		boolean tagged = isTagged(adaptable,tags);
		if(!tagged){
			final Holder holder = new Holder();
			
			// check the children of the resource to see if any of them are tagged
			try {
				final IJavaElement javaElt = (IJavaElement)adaptable.getAdapter(org.eclipse.jdt.core.IJavaElement.class);
				if(javaElt != null){
					final IResource resource = javaElt.getCorrespondingResource();
					if(resource != null){
						resource.accept(new IResourceVisitor(){
							public boolean visit(IResource resource) throws CoreException {
								if(!holder.accept){
									holder.accept = isTagged(resource, tags);
								}
								return(true);
							}
						});						
					}
				}
			} catch(CoreException ce){
				TaggerLog.error("Unable to filter resources: " + ce.getMessage(), ce);
			}
			
			tagged = holder.accept;
		}
		return(tagged);
	}
	
	@Override
	protected boolean isClosedProject(IAdaptable adaptable){
		final IJavaProject project = (IJavaProject)adaptable.getAdapter(IJavaProject.class);
		return(project != null && !project.isOpen());
	}
}
