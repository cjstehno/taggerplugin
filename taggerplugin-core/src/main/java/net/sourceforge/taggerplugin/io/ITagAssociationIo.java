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
import java.util.Map;

import net.sourceforge.taggerplugin.model.TagAssociation;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ITagAssociationIo {
	
	/**
	 * Used to read the tag association information from the Reader and create the tag objects.
	 *
	 * @param reader the reader
	 * @param monitor the progress monitor
	 * @return the tag associations contained in the reader
	 * @throws IOException if there is a problem reading the tags
	 */
	public Map<String, TagAssociation> readTagAssociations(Reader reader, IProgressMonitor monitor) throws IOException;

	/**
	 * Used to write the tag association data to the specified writer.
	 *
	 * @param writer the writer
	 * @param associations the tag associations to be written
	 * @param monitor the progress monitor
	 * @throws IOException if there is a problem writing the tags
	 */
	public void writeTagAssociations(Writer writer, Map<String,TagAssociation> associations, IProgressMonitor monitor) throws IOException;
}
