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

import java.util.UUID;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class TaggedResourceMarker implements ITaggedMarker {

	private final IMarker marker;
	
	TaggedResourceMarker(final IMarker marker){
		super();
		this.marker = marker;
	}

	public String getResourceId() throws CoreException {
		final String rcid = (String)marker.getAttribute(KEY_RESOURCEID);
		if(rcid == null){
			final String resourceId = UUID.randomUUID().toString();
			marker.setAttribute(KEY_RESOURCEID, resourceId);
			return(resourceId);
		} else {
			return(rcid);
		}
	}
	
	public IResource getResource(){
		return(marker.getResource());
	}

	public ITaggable getTaggableResource() {
		return((ITaggable)marker.getResource().getAdapter(ITaggable.class));
	}
}
