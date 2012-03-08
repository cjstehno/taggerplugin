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
package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.TagSelectionDialog;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * When triggered, this action will open a tag selection dialog to filter the associated view by the 
 * selected tag associtaions. The current associations used for filtering, if any, will be displayed.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class FilterUsingTagsAction implements IViewActionDelegate {

	private IViewPart view;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}

	/**
	 * @see IViewActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		final TreeViewer viewer = extractTreeViewer();
		TagAssociationFilter tagFilter = findTagFilter(viewer);
			
		final TagSelectionDialog dialog = new TagSelectionDialog(view.getSite().getShell(),TagManager.getInstance().getTags(),TaggerMessages.FilterUsingTagsAction_Title,TaggerMessages.FilterUsingTagsAction_Message);
		if(tagFilter != null){
			dialog.setInitialSelections(tagFilter.getTags());
		}
		if(dialog.open() == TagSelectionDialog.OK){
			final Object[] results = dialog.getResult();
			if(results != null && results.length != 0){
				if(tagFilter == null){
					// create a new filter populate it and add it
					tagFilter = createTagFilter();
					for(Object obj : results){
						tagFilter.addTag((Tag)obj);
					}
					viewer.addFilter(tagFilter);
				} else {
					// populate the filter that currently exists
					for(Object obj : results){
						tagFilter.addTag((Tag)obj);
					}
				}
			} else {
				if(tagFilter != null){
					// remove the filter that is no longer needed
					viewer.removeFilter(tagFilter);
				}
			}
		}
	}
	
	protected IViewPart getViewPart(){
		return(view);
	}

	/**
	 * Used to extract the TreeViewer from the view part.
	 * 
	 * @return the TreeViewer to be filtered
	 */
	protected TreeViewer extractTreeViewer() {
		TreeViewer viewer = null;
		if(view instanceof ResourceNavigator){
			viewer = ((ResourceNavigator)view).getViewer();	
		} else if(view instanceof CommonNavigator){
			viewer = ((CommonNavigator)view).getCommonViewer();	
		}
		return viewer;
	}
	
	protected TagAssociationFilter createTagFilter(){
		return(new TagAssociationFilter());
	}

	/**
	 * Used to find the tag filter in those registered to the viewer; if none is found
	 * a null value is returned.
	 *
	 * @param viewer the viewer
	 * @return a TagAssocitaionFilter if one is registered, or null
	 */
	private TagAssociationFilter findTagFilter(final TreeViewer viewer) {
		TagAssociationFilter tagFilter = null;
		final ViewerFilter[] filters = viewer.getFilters();
		for (ViewerFilter filter : filters) {
			if(filter instanceof TagAssociationFilter){
				tagFilter = (TagAssociationFilter)filter;
				break;
			}
		}
		return(tagFilter);
	}
}
