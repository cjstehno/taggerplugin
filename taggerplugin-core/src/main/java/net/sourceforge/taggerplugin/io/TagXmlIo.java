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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.util.XmlUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tag I/O handler for reading/writing tag data as XML.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagXmlIo implements ITagIo {

	private static final String ATTRNAME_ID = "id";
	private static final String ELT_DESCRIPTION = "description";
	private static final String ELT_NAME = "name";
	private static final String ELT_TAG = "tag";
	private static final String ELT_TAGS = "tags";
	private final DocumentBuilder builder;
	private Transformer transformer;

	TagXmlIo() throws ParserConfigurationException {
		super();
		this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	/**
	 * @see ITagIo#readTags(Reader, IProgressMonitor)
	 */
	public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException {
		final List<Tag> tags = new LinkedList<Tag>();
		try {
			final Document doc = builder.parse(new InputSource(reader));
			final Element tagsElt = doc.getDocumentElement();

			monitor.beginTask(TaggerMessages.TagIo_Reading,tagsElt.getChildNodes().getLength());

			Element tagElt = (Element)tagsElt.getFirstChild();
			while(tagElt != null){
				final Element tagNameElt = (Element)tagElt.getFirstChild();
				final Element tagDescElt = (Element)tagElt.getLastChild();

				tags.add(new Tag(tagElt.getAttribute(ATTRNAME_ID),tagNameElt.getTextContent(),tagDescElt.getTextContent()));

				monitor.worked(1);

				tagElt = (Element)tagElt.getNextSibling();
			}
		} catch(SAXException sex){
			throw new IOException(sex.getMessage());
		}
		return(tags.toArray(new Tag[tags.size()]));
	}

	/**
	 * @see ITagIo#writeTags(Writer, Tag[], IProgressMonitor)
	 */
	public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(TaggerMessages.TagIo_Writing,tags.length + 1);

		// build the tag document
		final Document doc = builder.newDocument();
		final Element tagsElt = XmlUtils.appendElement(doc, ELT_TAGS);
		for(Tag tag : tags){
			final Element tagElt = XmlUtils.appendElement(tagsElt, ELT_TAG);
			tagElt.setAttribute(ATTRNAME_ID,tag.getId().toString());

			final Element nameElt = XmlUtils.appendElement(tagElt,ELT_NAME);
			nameElt.appendChild(doc.createCDATASection(tag.getName()));

			final Element descElt = XmlUtils.appendElement(tagElt,ELT_DESCRIPTION);
			descElt.appendChild(doc.createCDATASection(tag.getDescription()));

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
