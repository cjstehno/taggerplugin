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
package net.sourceforge.taggerplugin.resource;

import org.eclipse.core.resources.IResource;

/**
 *	Interface used for adapting resources into taggable resources.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITaggable {

	/**
	 * Used to associate the tag with the specified id on the taggable object.
	 *
	 * @param id the id of the tag
	 */
	public void setTag(String id);

	/**
	 * Used to remove the association with the tag with the specified id.
	 *
	 * @param id the tag id
	 */
	public void clearTag(String id);

	/**
	 * Used to remove all tag associations from the tagged object.
	 */
	public void clearTags();

	/**
	 * Used to retrieve an array of the ids for each tag associated with the taggable object.
	 *
	 * @return an array of all tag ids associated with the taggable object
	 */
	public String[] listTags();

	/**
	 * Used to determine whether the tag with a specified id is associated with the taggable
	 * object.
	 *
	 * @param id the tag id
	 * @return a value of true if the tag with the specified id is associated with the taggable object.
	 */
	public boolean hasTag(String id);

	public boolean hasTags();
	
	public IResource getResource();
}
