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
package net.sourceforge.taggerplugin.util;

import org.eclipse.swt.widgets.Display;

public class SynchWithDisplay {

	/*
	 * This is a bit hacky but SWT is single-threaded and throws an error if you access the ui from another thread
	 * so this seems to be the correct way to do it. I have placed it in this util class to abstract it a bit for now.
	 */
	
	private SynchWithDisplay(){super();}

	public static final void synch(Runnable task){
		Display.getDefault().asyncExec(task);
	}
}
