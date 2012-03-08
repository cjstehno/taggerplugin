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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

/**
 *	Tag Association IO handler for reading/writing tag associations in Eclipse memento format. This handler
 *	should ONLY be used for internal storage, not externalization.
 * 
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationMementoIo implements ITagAssociationIo {

	private static final String TAG_ASSOCIATIONS = "associations";
	private static final String TAG_ASSOCIATION = "assoc";
	private static final String TAG_REFID = "ref-id";
	private static final String TAG_TAG = "tag";
	
	public Map<String, TagAssociation> readTagAssociations(Reader reader, IProgressMonitor monitor) throws IOException {
		final Map<String,TagAssociation> associations = new HashMap<String, TagAssociation>();
		try {
			final IMemento[] children = XMLMemento.createReadRoot(reader).getChildren(TAG_ASSOCIATION);
			for (IMemento mem : children) {
				final String resourceId = mem.getID();

				TagAssociation tagAssoc = associations.get(resourceId);
				if(tagAssoc == null){
					tagAssoc = new TagAssociation(resourceId);
					associations.put(resourceId, tagAssoc);
				}

				final IMemento[] resourceChildren = mem.getChildren(TAG_TAG);
				for(IMemento rchild : resourceChildren){
					tagAssoc.addTagId(rchild.getString(TAG_REFID));
				}
			}
		} catch(Exception ex){
			TaggerLog.error(ex);
		} finally {
			IoUtils.closeQuietly(reader);
		}
		return(associations);
	}

	public void writeTagAssociations(Writer writer, Map<String, TagAssociation> associations, IProgressMonitor monitor) throws IOException {
		if(associations == null){return;}

		final XMLMemento memento = XMLMemento.createWriteRoot(TAG_ASSOCIATIONS);
		for (Entry<String,TagAssociation> entry : associations.entrySet()) {
			final IMemento mem = memento.createChild(TAG_ASSOCIATION,String.valueOf(entry.getKey()));

			for (String tagId : entry.getValue()){
				final IMemento tagMem = mem.createChild(TAG_TAG);
				tagMem.putString(TAG_REFID, tagId.toString());
			}
		}

		memento.save(writer);
	}
}
