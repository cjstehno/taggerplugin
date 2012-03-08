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

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * ViewerFilter used to filter the ResourceNavigator and CommonNavigator so that only resources with 
 * the specified tags are shown.
 * 
 * This filter will accept elements whose children match the filter criteria so that child elements will 
 * not be obscured simply because their parents do not match the criteria.
 * 
 * Currently, this filter only matches resources that have any of the selected tag associations.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationFilter extends ViewerFilter {
	// TODO: the child-matching criteria may be a candidate for a preference, maybe some users would want the obscuring behavior
	
	protected final List<Tag> tags = new LinkedList<Tag>();
	
	/**
	 * @see ViewerFilter#select(Viewer, Object, Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		final IResource resource = (IResource)element;
		
		// closed projects fail fast
		if(isClosedProject(resource)){
			return(false);
		}
		
		boolean tagged = isTagged(resource,tags);
		if(!tagged){
			final Holder holder = new Holder();
			
			// check the children of the resource to see if any of them are tagged
			try {
				resource.accept(new IResourceVisitor(){
					public boolean visit(IResource resource) throws CoreException {
						if(!holder.accept){
							holder.accept = isTagged(resource, tags);
						}
						return(true);
					}
				});
			} catch(CoreException ce){
				TaggerLog.error("Unable to filter resources: " + ce.getMessage(), ce);
			}
			
			tagged = holder.accept;
		}
		return(tagged);
	}

	/**
	 * Used to add accepted tags to the view filter.
	 *
	 * @param tag the tag to be added
	 */
	void addTag(Tag tag){
		tags.add(tag);
	}
	
	/**
	 * Used to retrieve the tags accepted by the filter.
	 *
	 * @return the accepted tags
	 */
	Tag[] getTags(){
		return(tags.toArray(new Tag[tags.size()]));
	}
	
	/**
	 * Used to determine if the given resource is tagged with any of the given tags.
	 *
	 * @param adaptable the resource being tested
	 * @param tags the tags
	 * @return a value of true if the resource is tagged
	 */
	protected static boolean isTagged(final IAdaptable adaptable, final List<Tag> tags){
		final ITaggable taggable = (ITaggable)adaptable.getAdapter(ITaggable.class);
		if(taggable != null){
			for(Tag tag : tags){
				if(taggable.hasTag(tag.getId())){
					return(true);
				}
			}
		}
		return(false);
	}
	
	/**
	 * Used to determine if the resource is a closed project.
	 *
	 * @param resource the resource to be tested
	 * @return true if the resource is a project that is closed
	 */
	protected boolean isClosedProject(IAdaptable adaptable){
		final IProject project = (IProject)adaptable.getAdapter(IProject.class);
		return(project != null && !project.isOpen());
	}
	
	/**
	 * A holder for a boolean to be dynamically modified.
	 *
	 * @author Christopher J. Stehno (chris@stehno.com)
	 */
	protected static final class Holder {
		public boolean accept = false;
		
		public Holder(){}
	}
}