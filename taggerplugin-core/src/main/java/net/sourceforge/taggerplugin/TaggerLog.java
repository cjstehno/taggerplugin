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
package net.sourceforge.taggerplugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Common access point for message logging.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerLog {

	private TaggerLog(){super();}

	/**
	 * Log an information message. 
	 *
	 * @param msg the message
	 */
	public static void info(String msg){
		log(IStatus.INFO, IStatus.OK, msg, null);
	}

	/**
	 * Log an error message.
	 *
	 * @param exception the exception to be logged
	 */
	public static void error(Throwable exception) {
		error("Unexpected Exception: " + exception.getMessage(), exception);
	}

	/**
	 * Log an error message with message and exception.
	 *
	 * @param message the message
	 * @param exception the exception
	 */
	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	/**
	 * Log a warning message.
	 *
	 * @param exception the exception
	 */
	public static void warn(Throwable exception) {
		warn("Unexpected Exception: " + exception.getMessage(), exception);
	}

	/**
	 * Log a warning message.
	 *
	 * @param message the message 
	 * @param exception the exception
	 */
	public static void warn(String message, Throwable exception) {
		log(IStatus.WARNING, IStatus.OK, message, exception);
	}

	/**
	 * Append a log entry to the log.
	 *
	 * @param severity the severity code
	 * @param code the error code
	 * @param message the message
	 * @param exception the exception
	 */
	public static void log(int severity, int code, String message,Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	/**
	 * Used to create a status object from the given logging information.
	 *
	 * @param severity the severity code
	 * @param code the error code
	 * @param message the message
	 * @param exception the exception
	 * @return a status object wrapping the given information
	 */
	private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, TaggerActivator.PLUGIN_ID, code,message, exception);
	}

	/**
	 * Perform the actual logging using the status.
	 *
	 * @param status the status representing the log information
	 */
	private static void log(IStatus status) {
		TaggerActivator.getDefault().getLog().log(status);
	}
}
