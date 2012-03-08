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
package net.sourceforge.taggerplugin.manager;

/**
 * Runtime exception used to denote problems during tag association operations.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationException extends RuntimeException {

	private static final long serialVersionUID = -3106727165779954211L;

	/**
	 *	@see RuntimeException#RuntimeException()
	 */
	public TagAssociationException() {
		super();
	}

	/**
	 *	@see RuntimeException#RuntimeException(String)
	 */
	public TagAssociationException(String msg) {
		super(msg);
	}

	/**
	 *	@see RuntimeException#RuntimeException(Throwable)
	 */
	public TagAssociationException(Throwable cause) {
		super(cause);
	}

	/**
	 *	@see RuntimeException#RuntimeException(String,Throwable)
	 */
	public TagAssociationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
