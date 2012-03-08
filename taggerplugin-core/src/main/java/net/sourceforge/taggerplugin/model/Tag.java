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
package net.sourceforge.taggerplugin.model;

import java.util.UUID;


/**
 *	Model object representing a single tag in the tag set.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class Tag implements Comparable<Tag> {

	private String id, name, description;
	
	public Tag(){
		super();
		this.id = UUID.randomUUID().toString();
	}
	
	public Tag(String name, String description){
		this();
		this.name = name;
		this.description = description;
	}

	public Tag(String id, String name, String description){
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getDescription() {
		return(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return("Tag: { id=" + id + ", name=[" + name + "], description=[" + description + "]}");
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Tag other = (Tag) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Tag tag){
		return(name.compareTo(tag.getName()));
	}
}
