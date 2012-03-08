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
package net.sourceforge.taggerplugin.search;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the tableviewer in the Tag Search results page.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchResultsViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final String DELIM = ", ";
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_PATH = 1;
	private static final int COLUMN_TAGS = 2;

	TagSearchResultsViewLabelProvider(){
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		final IResource resource = (IResource)element;
		switch(columnIndex){
			case COLUMN_NAME:
				return(resource.getName());
			case COLUMN_PATH:
				return(String.valueOf(resource.getLocation()));
			case COLUMN_TAGS:
				final Tag[] tags = TagManager.getInstance().findTags(((ITaggable)resource.getAdapter(ITaggable.class)).listTags());
				return(TagManager.extractTagNames(tags,DELIM));
			default:
				return("???");
		}
	}
}