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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Tag I/O handler for reading/writing tag information as CSV data. The first line of data will be a 
 * header.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagCsvIo implements ITagIo {

	private static final char LINEBREAK = '\n';
	private static final String QUOTE_COMMA_QUOTE = "\",\"";
	private static final char QUOTE = '"';
	private static final String HEADER_LINE = "\"Id\",\"Name\",\"Description\"";
	
	/**
	 * Creates a tag csv io instance.
	 */
	TagCsvIo(){
		super();
	}

	/**
	 * @see ITagIo#readTags(Reader, IProgressMonitor)
	 */
	public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Reading, 1);	// not very accurate but not sure what else can be used

		final List<Tag> tags = new LinkedList<Tag>();

		final BufferedReader breader = (BufferedReader)reader;
		for(String line = breader.readLine(); line != null; line = breader.readLine()){
			if(!line.equalsIgnoreCase(HEADER_LINE)){
				final String[] parts = (line.substring(1,line.length()-1)).split(QUOTE_COMMA_QUOTE);
				tags.add(new Tag(parts[0],parts[1],parts[2]));
			}
		}

		monitor.worked(1);

		return(tags.toArray(new Tag[tags.size()]));
	}

	/**
	 * @see ITagIo#writeTags(Writer, Tag[], IProgressMonitor)
	 */
	public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Writing, tags.length);

		final BufferedWriter bwriter = (BufferedWriter)writer;
		bwriter.write(HEADER_LINE);
		bwriter.newLine();

		final StringBuilder str = new StringBuilder();
		for(Tag tag : tags){
			str.append(QUOTE).append(tag.getId().toString()).append(QUOTE_COMMA_QUOTE)
				.append(tag.getName()).append(QUOTE_COMMA_QUOTE)
				.append(tag.getDescription()).append(QUOTE).append(LINEBREAK);

			bwriter.write(str.toString());

			monitor.worked(1);

			str.delete(0, str.length());
		}
	}
}
