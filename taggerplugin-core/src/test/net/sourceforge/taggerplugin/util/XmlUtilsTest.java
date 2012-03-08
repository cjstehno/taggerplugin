package net.sourceforge.taggerplugin.util;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtilsTest {
	
	private static final String TAG_CHILD = "child";
	private static final String TAG_DUMMY = "dummy";
	private Document document;
	
	@Before
	public void createDocument() throws Exception {
		this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}
	
	@Test
	public void appendElementToNode(){
		final Element elt = (Element)document.appendChild(document.createElement(TAG_DUMMY));
		
		final Element child = XmlUtils.appendElement(elt,TAG_CHILD);
		
		Assert.assertNotNull(child);
		Assert.assertEquals(TAG_CHILD, child.getTagName());
		Assert.assertEquals(elt, child.getParentNode());
	}
	
	@Test
	public void appendElementToDocument(){
		final Element child = XmlUtils.appendElement(document, TAG_CHILD);
		
		Assert.assertNotNull(child);
		Assert.assertEquals(TAG_CHILD, child.getTagName());
		Assert.assertEquals(document, child.getParentNode());
	}

	@After
	public void destroyDocument(){
		this.document = null;
	}
	
}
