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
package net.sourceforge.taggerplugin.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;

public class GenericViewSorter extends ViewerSorter {

	private static final String TAG_DESCENDING = "descending";
	private static final String TAG_COLUMNIDX = "columnIndex";
	private static final String TAG_TYPE = "-view-sorter";
	private static final String TAG_TRUE = "true";

	private final TableViewer viewer;
	private final String id;
	private ColumnSortDescriptor[] sortDescriptors;

	public GenericViewSorter(String id,TableViewer viewer,TableColumn[] columns, Comparator[] comparators){
		this.id = id;
		this.viewer = viewer;
		sortDescriptors = new ColumnSortDescriptor[columns.length];
		for (int i = 0; i < columns.length; i++) {
			final ColumnSortDescriptor csd = new ColumnSortDescriptor(i,comparators[i],false); 
			sortDescriptors[i] = csd;
			
			columns[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					sortUsing(csd);
				}
			});
		}
	}
	
	public void init(IMemento memento) {
		List<ColumnSortDescriptor> newCsds = new ArrayList<ColumnSortDescriptor>(sortDescriptors.length);
		IMemento[] mems = memento.getChildren(id + TAG_TYPE);
		for(IMemento mem : mems){
			final Integer colIdx = mem.getInteger(TAG_COLUMNIDX);
			if (colIdx == null) continue;
			
			int index = colIdx.intValue();
			if (index < 0 || index >= sortDescriptors.length) continue;
			
			ColumnSortDescriptor csd = sortDescriptors[index];
			if (newCsds.contains(csd)) continue;
			
			csd.descending = TAG_TRUE.equals(mem.getString(TAG_DESCENDING));
			newCsds.add(csd);
		}
		for (int i = 0; i < sortDescriptors.length; i++){
			if (!newCsds.contains(sortDescriptors[i])){
				newCsds.add(sortDescriptors[i]);
			}
		}
		sortDescriptors = newCsds.toArray(new ColumnSortDescriptor[newCsds.size()]);
	}

	@SuppressWarnings("unchecked")
	public int compare(Viewer viewer, Object tag1, Object tag2) {
		for (int i = 0; i < sortDescriptors.length; i++) {
			int result = sortDescriptors[i].comparator.compare(tag1,tag2);
			if (result != 0) {
				if (sortDescriptors[i].descending){
					return(-result);
				}
				return(result);
			}
		}
		return(0);
	}

	protected void sortUsing(ColumnSortDescriptor csd){
		if (csd == sortDescriptors[0]){
			csd.descending = !csd.descending;
		} else {
			for (int i = 0; i < sortDescriptors.length; i++) {
				if (csd == sortDescriptors[i]) {
					System.arraycopy(sortDescriptors, 0, sortDescriptors, 1, i);
					sortDescriptors[0] = csd;
					csd.descending = false;
					break;
				}
			}
		}
		viewer.refresh();
	}

	public void saveState(IMemento memento) {
		for(ColumnSortDescriptor csd : sortDescriptors){
			IMemento mem = memento.createChild(id + TAG_TYPE);
			mem.putInteger(TAG_COLUMNIDX, csd.columnIndex);
			if (csd.descending){
				mem.putString(TAG_DESCENDING, TAG_TRUE);
			}
		}
	}

	private static class ColumnSortDescriptor {
		
		private final int columnIndex;
		private final Comparator comparator;
		private boolean descending;
		
		private ColumnSortDescriptor(int columnIndex,Comparator comparator,boolean descending){
			this.columnIndex = columnIndex;
			this.comparator = comparator;
			this.descending = descending;
		}
	}
}
