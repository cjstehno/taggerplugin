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

import net.sourceforge.taggerplugin.event.TagSearchResultEvent;
import net.sourceforge.taggerplugin.event.TagSearchResultEvent.Type;
import net.sourceforge.taggerplugin.util.SynchWithDisplay;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

/**
 * Content provider for the tableviewer in the Tag Search Result page.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchResultsViewContentProvider implements IStructuredContentProvider,ISearchResultListener {

	private TagSearchResult result;
	private TableViewer viewer;

	TagSearchResultsViewContentProvider(){
		super();
	}

	public Object[] getElements(Object inputElement) {
		return(result.getMatches());
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer)viewer;

		if(result != null){
			// remove the listener from the old input
			result.removeListener(this);
		}

		if(newInput != null){
			result = (TagSearchResult)newInput;
			result.addListener(this);
		}
	}

	public void searchResultChanged(SearchResultEvent e) {
		final TagSearchResultEvent evt = (TagSearchResultEvent)e;

		SynchWithDisplay.synch(new Runnable(){
			public void run() {
				viewer.getTable().setRedraw(false);
				try {
					if(evt.getType().equals(Type.ADDED)){
						viewer.add(evt.getResources());
					} else if(evt.getType().equals(Type.REMOVED)){
						viewer.remove(evt.getResources());
					}
				} finally {
					viewer.getTable().setRedraw(true);
				}
			}
		});
	}
}