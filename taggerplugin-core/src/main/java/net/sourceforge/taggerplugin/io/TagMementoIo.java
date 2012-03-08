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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

/**
 * Tag I/O handler for reading/writing tag data using the Eclipse memento API.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagMementoIo implements ITagIo {
	
	private static final String TAG_TAGS = "tags";
	private static final String TAG_TAG = "tag";
	private static final String TAG_NAME = "name";
	
	TagMementoIo() {
		super();
	}

	/**
	 * @see ITagIo#readTags(Reader, IProgressMonitor)
	 */
	public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException {
		final List<Tag> tags = new LinkedList<Tag>();
		try {
			final IMemento [] children = XMLMemento.createReadRoot(reader).getChildren(TAG_TAG);
			for (IMemento mem : children) {
				final Tag tag = new Tag(mem.getID(), mem.getString(TAG_NAME), mem.getTextData());
				tags.add(tag);
			}
		} catch(WorkbenchException we){
			throw new IOException(we.getMessage());
		}
		return(tags.toArray(new Tag[tags.size()]));
	}

	/**
	 * @see ITagIo#writeTags(Writer, Tag[], IProgressMonitor)
	 */
	public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException {
		final XMLMemento memento = XMLMemento.createWriteRoot(TAG_TAGS);
		for (Tag tag : tags) {
			final IMemento mem = memento.createChild(TAG_TAG,String.valueOf(tag.getId()));
			mem.putString(TAG_NAME, tag.getName());
			mem.putTextData(tag.getDescription());
		}
		
		memento.save(writer);
	}
}
