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
package net.sourceforge.taggerplugin.event;

import java.util.EventListener;

/**
 * Listener for clients that want to recieve tag association events.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITagAssociationManagerListener extends EventListener {

	/**
	 * Allows clients to handle tag association events.
	 *
	 * @param tae the tag association event fired
	 */
	public void handleTagAssociationEvent(TagAssociationEvent tae);
}
