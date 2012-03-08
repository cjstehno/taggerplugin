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

import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;

/**
 * Event fired when tag associations are added or removed from resources.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationEvent extends EventObject {

	public static enum Type {ADDED,REMOVED};

	private static final long serialVersionUID = -3523425086390468129L;
	private final Type type;
	private final IResource resource;

	/**
	 * Creates a new tag association event.
	 * 
	 * @param manager tag association manager
	 * @param type the type of event
	 * @param resource the resource being modified
	 */
	public TagAssociationEvent(TagAssociationManager manager, Type type, IResource resource){
		super(manager);
		this.type = type;
		this.resource = resource;
	}

	/**
	 * Used to retrieve the tag associaion manager.
	 *
	 * @return the tag association manager
	 */
	public TagAssociationManager getManager(){return((TagAssociationManager)getSource());}

	/**
	 * Retrieves the type of event.
	 *
	 * @return the event type
	 */
	public Type getType(){return(type);}

	/**
	 * Retrieves the resource being affected.
	 *
	 * @return the affected resource
	 */
	public IResource getResource() {
		return this.resource;
	}
}
