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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlUtils {
	
	private XmlUtils(){super();}
	
	/**
	 * Appends a child element with the given tag name to the specified parent node. If the parent node 
	 * is the Document, it is used itself.
	 *
	 * @param parent the parent node
	 * @param tagName the tag name
	 * @return the new element with the given tag name that is a child of the parent node
	 */
	public static Element appendElement(Node parent, String tagName){
		return((Element)parent.appendChild((parent.getOwnerDocument() != null ? parent.getOwnerDocument() : (Document)parent).createElement(tagName)));
	}
}
