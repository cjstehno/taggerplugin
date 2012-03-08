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
package net.sourceforge.taggerplugin.io;

/**
 * Factory for creating Tag association I/O handlers based on desired format type.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationIoFactory {

	private TagAssociationIoFactory(){super();}

	/**
	 * Creates the appropriate handler based on the format type.
	 *
	 * @param format the data format
	 * @return the handler for the data format type
	 * @throws Exception if there is a problem
	 */
	public static final ITagAssociationIo create(TagIoFormat format) throws Exception {
		if(format.equals(TagIoFormat.XML)){
			return(new TagAssociationXmlIo());
		} else if(format.equals(TagIoFormat.CSV)){
			return(new TagAssociationCsvIo());
		} else if(format.equals(TagIoFormat.MEMENTO)){
			return(new TagAssociationMementoIo());
		}
		return(null);
	}
}
