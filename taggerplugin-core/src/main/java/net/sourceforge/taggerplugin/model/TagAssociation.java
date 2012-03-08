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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TagAssociation implements Iterable<String> {

	private String resourceId;
	private Set<String> tagIds;
	
	public TagAssociation(){
		super();
		this.tagIds = new HashSet<String>();
	}
	
	public TagAssociation(String resourceId){
		this();
		this.resourceId = resourceId;
	}
	
	public String getResourceId(){
		return this.resourceId;
	}

	public Iterator<String> iterator() {
		return tagIds.iterator();
	}
	
	public String[] getAssociations(){
		return(tagIds.toArray(new String[tagIds.size()]));
	}
	
	public void addTagId(String tagid){
		tagIds.add(tagid);
	}
	
	public boolean hasAssociations(){
		return(!tagIds.isEmpty());
	}
	
	public boolean isEmpty(){
		return(tagIds.isEmpty());
	}
	
	public boolean containsAssociation(String tagid){
		return(tagIds.contains(tagid));
	}
	
	public boolean removeAssociation(String tagid){
		return(tagIds.remove(tagid));
	}
	
	public boolean removeAssociations(String[] tagids){
		return(tagIds.removeAll(Arrays.asList(tagids)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.resourceId == null) ? 0 : this.resourceId.hashCode());
		result = PRIME * result + ((this.tagIds == null) ? 0 : this.tagIds.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		final TagAssociation other = (TagAssociation) obj;
		if (this.resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!this.resourceId.equals(other.resourceId))
			return false;
		if (this.tagIds == null) {
			if (other.tagIds != null)
				return false;
		} else if (!this.tagIds.equals(other.tagIds))
			return false;
		return true;
	}
}
