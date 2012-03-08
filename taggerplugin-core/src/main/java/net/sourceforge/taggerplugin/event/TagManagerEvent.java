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
package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

/**
 * Event fired by the TagManager when managed tags set changes.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagManagerEvent extends EventObject {

	public static enum Type {ADDED,REMOVED,UPDATED};

	private static final long serialVersionUID = 1554238208483233563L;
	private final Tag[] tags;
	private final Type type;

	/**
	 * Creates a new tag manager event.
	 * 
	 * @param manager the tag manager
	 * @param type the type of event
	 * @param tags the tags affected
	 */
	public TagManagerEvent(final TagManager manager,final Type type,final Tag[] tags){
		super(manager);
		this.tags = tags;
		this.type = type;
	}

	/**
	 * Retrieves the type of event represented.
	 *
	 * @return the type of event
	 */
	public Type getType() {return(type);}

	/**
	 * Retrieves the affected tags.
	 *
	 * @return the tags
	 */
	public Tag[] getTags(){return(tags);}

	/**
	 * Retrieves the tag manager.
	 *
	 * @return the tag manager
	 */
	public TagManager getTagManager(){return((TagManager)getSource());}
}
