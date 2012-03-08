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

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class RemovedResourceDeltaVisitor implements IResourceDeltaVisitor {

	public boolean visit(IResourceDelta delta) throws CoreException {
		int kind = delta.getKind();
		if(kind == IResourceDelta.REMOVED){
			final IMarkerDelta[] markers = delta.getMarkerDeltas();
			if(markers != null){
				for(IMarkerDelta md : markers){
					if(md.getKind() == IResourceDelta.REMOVED && md.getType() == ITaggedMarker.MARKER_TYPE){
						final String rcid = (String)md.getAttribute(ITaggedMarker.KEY_RESOURCEID);
						if(rcid != null){
							TagAssociationManager.getInstance().deleteAssociations(rcid);							
						}
					}
				}	
			}
		}
		return true;
	}

}
