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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public interface ITaggedMarker {

	public static final String MARKER_TYPE = "com.sourceforge.taggerplugin.tagged";
	public static final String KEY_RESOURCEID = "resourceId";
	
	public String getResourceId() throws CoreException;
	
	public IResource getResource();
	
	public ITaggable getTaggableResource();
}
