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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.resource.ITaggedMarker;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;
import net.sourceforge.taggerplugin.util.XmlUtils;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TagAssociationXmlIo implements ITagAssociationIo {
	
	private static final String ELT_ASSOCIATIONS = "associations";
	private static final String ELT_ASSOC = "assoc";
	private static final String ELT_TAG = "tag";
	private static final String ATTRNAME_PATH = "path";
	private static final String ATTRNAME_REF = "ref";
	private final DocumentBuilder builder;
	private Transformer transformer;
	
	public TagAssociationXmlIo() throws ParserConfigurationException {
		super();
		this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	public Map<String, TagAssociation> readTagAssociations(Reader reader, IProgressMonitor monitor) throws IOException {
		final Map<String,TagAssociation> associations = new HashMap<String, TagAssociation>();
		try {
			final Document doc = builder.parse(new InputSource(reader));
			final Element associationsElt = doc.getDocumentElement();

			monitor.beginTask(TaggerMessages.TagIo_Reading,associationsElt.getChildNodes().getLength());

			Element assocElt = (Element)associationsElt.getFirstChild();
			while(assocElt != null){
				final IPath path = Path.fromPortableString(assocElt.getAttribute(ATTRNAME_PATH));
				final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
				
				if(resource != null){
					final ITaggedMarker marker = TaggedMarkerHelper.getOrCreateMarker(resource);
					
					TagAssociation assoc = null;
					if(associations.containsKey(marker.getResourceId())){
						assoc = associations.get(marker.getResourceId());
					} else {
						assoc = new TagAssociation(marker.getResourceId());						
					}
					
					Element tagElt = (Element)assocElt.getFirstChild();
					while(tagElt != null){
						assoc.addTagId(tagElt.getAttribute(ATTRNAME_REF));
						
						tagElt = (Element)tagElt.getNextSibling();
					}
					
					associations.put(marker.getResourceId(),assoc);
				}

				monitor.worked(1);

				assocElt = (Element)assocElt.getNextSibling();
			}
		} catch(SAXException sex){
			throw new IOException(sex.getMessage());
		} catch (CoreException ce){
			throw new IOException(ce.getMessage());
		}
		return(associations);
	}

	public void writeTagAssociations(Writer writer, Map<String, TagAssociation> associations, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Writing,associations.size() + 1);

		// build the tag document
		final Document doc = builder.newDocument();
		final Element associationsElt = XmlUtils.appendElement(doc, ELT_ASSOCIATIONS);
		
		try {
			for(Entry<String, TagAssociation> entry : associations.entrySet()){
				final IResource resource = TaggedMarkerHelper.getResource(entry.getValue().getResourceId());
				if(resource != null){
					final Element assocElt = XmlUtils.appendElement(associationsElt, ELT_ASSOC);
					assocElt.setAttribute(ATTRNAME_PATH,resource.getFullPath().toPortableString());
		
					for(String tagid : entry.getValue().getAssociations()){
						final Element tagElt = XmlUtils.appendElement(assocElt,ELT_TAG);
						tagElt.setAttribute(ATTRNAME_REF,tagid);
					}				
				}
	
				monitor.worked(1);
			}
	
			// serialize the tag document
			try {
				getTransformer().transform(new DOMSource(doc), new StreamResult(writer));
			} catch(TransformerException te){
				throw new IOException(te.getMessage());
			} finally {
				monitor.worked(1);
			}
			
		} catch(CoreException ce){
			TaggerLog.error(ce);
			throw new IOException("Unable to write associations: " + ce.getMessage());
		}
	}
	
	/**
	 * Used to retrieve the common transformer.
	 *
	 * @return the transformer
	 * @throws TransformerConfigurationException if there is a problem building the transformer
	 */
	private Transformer getTransformer() throws TransformerConfigurationException {
		if(transformer == null){
			transformer = TransformerFactory.newInstance().newTransformer();
		}
		return(transformer);
	}
}
