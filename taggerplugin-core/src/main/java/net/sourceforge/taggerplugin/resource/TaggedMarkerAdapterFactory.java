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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IAdapterFactory;

public class TaggedMarkerAdapterFactory implements IAdapterFactory {
	
	private static final Class[] ADAPTERS = {ITaggedMarker.class};

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return(new TaggedResourceMarker((IMarker)adaptableObject));
	}

	public Class[] getAdapterList() {
		return(ADAPTERS);
	}
}
