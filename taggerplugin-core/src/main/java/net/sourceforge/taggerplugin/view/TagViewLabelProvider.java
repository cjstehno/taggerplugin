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
package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Provided the view labels for the TagView. Maps the Tag model fields with the
 * table columns of the view and provides any necessary conversion/translation.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final int INDEX_NAME = 0;
	private static final int INDEX_DESCRIPTION = 1;

	public String getColumnText(Object obj, int index) {
		final Tag tag = (Tag)obj;
		String txt = null;
		switch(index){
			case INDEX_NAME:
				txt = tag.getName();
				break;
			case INDEX_DESCRIPTION:
				txt = tag.getDescription();
				break;
			default:
				txt = "???";
				break;
		};
		return(txt);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;	// no image
	}
}
