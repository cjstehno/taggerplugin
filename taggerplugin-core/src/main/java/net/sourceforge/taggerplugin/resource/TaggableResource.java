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
package net.sourceforge.taggerplugin.resource;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;

/**
 * Implentation of the ITaggable interface that delegates to the internally held
 * IResource implementation and the TagAssociationManager.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TaggableResource implements ITaggable {

	private final IResource resource;

	TaggableResource(final IResource resource) {
		this.resource = resource;
	}

	public void setTag(String id) {
		TagAssociationManager.getInstance().addAssociation(resource, id);
	}

	public void clearTag(String id) {
		TagAssociationManager.getInstance().clearAssociation(resource, id);
	}

	public void clearTags() {
		TagAssociationManager.getInstance().clearAssociations(resource);
	}

	public boolean hasTag(String id) {
		return(TagAssociationManager.getInstance().hasAssociation(resource, id));
	}

	public String[] listTags() {
		return(TagAssociationManager.getInstance().getAssociations(resource));
	}

	public boolean hasTags(){
		return(TagAssociationManager.getInstance().hasAssociations(resource));
	}
	
	public IResource getResource(){return(resource);}
}