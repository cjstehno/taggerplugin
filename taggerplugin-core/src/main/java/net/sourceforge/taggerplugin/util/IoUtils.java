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

import java.io.Reader;
import java.io.Writer;

/**
 * 
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class IoUtils {
	// may want to import equivalent jakarta-commons api, but for now...
	
	private IoUtils(){super();}

	public static void closeQuietly(final Reader reader){
		if(reader != null){
			try {reader.close();} catch(Exception e){/* ignored */}
		}
	}

	public static void closeQuietly(final Writer writer) {
		if(writer != null){
			try {writer.close();} catch(Exception e){/* ignored */}
		}
	}
}
