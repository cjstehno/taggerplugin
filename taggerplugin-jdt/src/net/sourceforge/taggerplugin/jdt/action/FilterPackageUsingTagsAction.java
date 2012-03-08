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
package net.sourceforge.taggerplugin.jdt.action;

import net.sourceforge.taggerplugin.action.FilterUsingTagsAction;
import net.sourceforge.taggerplugin.action.TagAssociationFilter;

import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Extension of the view filtering action used by the Resource Tagger plug-in to filter
 * view elements by their tag associations.
 * 
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class FilterPackageUsingTagsAction extends FilterUsingTagsAction {

	/**
	 * @see FilterUsingTagsAction#extractTreeViewer()
	 */
	@Override
	protected TreeViewer extractTreeViewer() {
		TreeViewer viewer = null;
		if(getViewPart() instanceof IPackagesViewPart){
			viewer = ((IPackagesViewPart)getViewPart()).getTreeViewer();	
		}
		return viewer;
	}

	/**
	 * @see FilterUsingTagsAction#createTagFilter()
	 */
	@Override
	protected TagAssociationFilter createTagFilter() {
		return(new PackageTagAssociationFilter());
	}
}
