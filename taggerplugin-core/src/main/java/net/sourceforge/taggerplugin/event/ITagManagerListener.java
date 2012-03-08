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
 * Listener for objects that need to be notified about tag manager events.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITagManagerListener extends EventListener {

	/**
	 * Used to allow the listener to handle tag manager events.
	 *
	 * @param tme the tag manager event fired
	 */
	public void handleTagManagerEvent(TagManagerEvent tme);
}
