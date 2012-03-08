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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.resource.ITaggedMarker;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 *
 * Note: care must be taken when using this handler as the read method does affect the workspace during 
 * its runtime.
 * 
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationCsvIo implements ITagAssociationIo {

	private static final char LINEBREAK = '\n';
	private static final String QUOTE_COMMA_QUOTE = "\",\"";
	private static final char QUOTE = '"';
	private static final String HEADER_LINE = "\"Resource\",\"Tag Ids\"";
	
	public Map<String, TagAssociation> readTagAssociations(Reader reader, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Reading, 1);	// not very accurate but not sure what else can be used

		final Map<String,TagAssociation> associations = new HashMap<String, TagAssociation>();

		final BufferedReader breader = (BufferedReader)reader;
		
		try {
			for(String line = breader.readLine(); line != null; line = breader.readLine()){
				if(!line.equalsIgnoreCase(HEADER_LINE)){
					final String[] parts = (line.substring(1,line.length()-1)).split(QUOTE_COMMA_QUOTE);
					
					final IPath path = Path.fromPortableString(parts[0]);
					final String[] tagids = parts[1].split(";");
					
					final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
					if(resource != null){
						final ITaggedMarker marker = TaggedMarkerHelper.getOrCreateMarker(resource);
	
						TagAssociation ta = null;
						if(associations.containsKey(marker.getResourceId())){
							ta = associations.get(marker.getResourceId());
						} else {
							ta = new TagAssociation(marker.getResourceId());
						}
						
						for(String tagid : tagids){
							ta.addTagId(tagid);
						}
						
						associations.put(marker.getResourceId(), ta);					
					}
				}
			}
	
			monitor.worked(1);
	
			return(associations);
		} catch(CoreException ce){
			TaggerLog.error(ce);
			throw new IOException("Unable to read associations: " + ce.getMessage());
		}
	}

	public void writeTagAssociations(Writer writer, Map<String, TagAssociation> associations, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Writing, associations.size());

		final BufferedWriter bwriter = (BufferedWriter)writer;
		bwriter.write(HEADER_LINE);
		bwriter.newLine();

		final StringBuilder str = new StringBuilder();
		
		try {
			for(Entry<String, TagAssociation> entry : associations.entrySet()){
				final IResource resource = TaggedMarkerHelper.getResource(entry.getKey());
				if(resource != null){
					str.append(QUOTE).append(resource.getFullPath().toPortableString()).append(QUOTE_COMMA_QUOTE);
					
					for(String tagid : entry.getValue().getAssociations()){
						str.append(tagid).append(";");
					}
					
					str.delete(str.lastIndexOf(";"), str.length());
					str.append(QUOTE).append(LINEBREAK);
		
					bwriter.write(str.toString());				
				}
	
				monitor.worked(1);
	
				str.delete(0, str.length());
			}
		} catch(CoreException ce){
			TaggerLog.error(ce);
			throw new IOException("Unable to write associations: " + ce.getMessage());
		}
	}
}
