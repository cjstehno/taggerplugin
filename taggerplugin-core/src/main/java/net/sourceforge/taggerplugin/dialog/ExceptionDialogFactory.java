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
package net.sourceforge.taggerplugin.dialog;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.swt.widgets.Shell;

/**
 * Helper utility for creating exception dialogs.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ExceptionDialogFactory {

	private ExceptionDialogFactory(){super();}

	/**
	 * Creates an exception dialog with the exception message for text and the exception stack trace as details.
	 *
	 * @param shell the shell
	 * @param ex the exception
	 * @return a populated {@link ExceptionDetailsDialog}
	 */
	public static final ExceptionDetailsDialog create(Shell shell, Throwable ex){
		return(create(shell,TaggerMessages.ExceptionDialogFactory_Title,ex.getMessage(),ex));
	}
	
	/**
	 * Creates an exception dialog with the given title, and message text. The exception stack trace is used
	 * for the details.
	 *
	 * @param shell the shell
	 * @param title the dialog title
	 * @param msg the dialog message
	 * @param ex the exception
	 * @return a populated {@link ExceptionDetailsDialog}
	 */
	public static final ExceptionDetailsDialog create(Shell shell, String title, String msg, Throwable ex){
		return(new ExceptionDetailsDialog(shell,title,null,msg,ex,TaggerActivator.getDefault()));
	}
}
