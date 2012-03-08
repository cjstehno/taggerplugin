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
package net.sourceforge.taggerplugin.search;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.event.TagSearchResultEvent;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

/**
 * Tag Search Result container.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSearchResult implements ITagSearchResult {

	private final Set<ISearchResultListener> listeners;
	private ISearchQuery query;
	private Set<IResource> matches;

	public TagSearchResult(){
		super();
		this.listeners = new HashSet<ISearchResultListener>();
		this.matches = Collections.synchronizedSet(new HashSet<IResource>());
	}

	void setQuery(ISearchQuery query){this.query = query;}

	public void addMatch(IResource resource){
		if(matches.add(resource)){
			fireSearchResultEvent(new TagSearchResultEvent(this,new IResource[]{resource},TagSearchResultEvent.Type.ADDED));
		}
	}

	public int getMatchCount(){
		return(matches.size());
	}

	public IResource[] getMatches(){
		return(matches.toArray(new IResource[matches.size()]));
	}

	public void clearMatches(){
		if(!matches.isEmpty()){
			final IResource[] removedResources = matches.toArray(new IResource[matches.size()]);
			matches.clear();
			fireSearchResultEvent(new TagSearchResultEvent(this,removedResources,TagSearchResultEvent.Type.REMOVED));
		}
	}

	public void addListener(ISearchResultListener l) {
		listeners.add(l);
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getLabel() {
		return TaggerMessages.TagSearchResult_Label;
	}

	public ISearchQuery getQuery() {
		return query;
	}

	public String getTooltip() {
		return TaggerMessages.TagSearchResult_Tooltip;
	}

	public void removeListener(ISearchResultListener l) {
		listeners.remove(l);
	}

	private void fireSearchResultEvent(SearchResultEvent sre){
		for(ISearchResultListener listener : listeners){
			listener.searchResultChanged(sre);
		}
	}
}