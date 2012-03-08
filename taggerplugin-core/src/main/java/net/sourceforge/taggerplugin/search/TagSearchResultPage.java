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


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.util.MementoUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.IContextMenuConstants;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.part.Page;

/**
 * Search results page viewer for the Tag Search.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSearchResultPage extends Page implements ISearchResultPage {

	private static final String TAG_COLWIDTH_TAGS = "col-width-tags";
	private static final String TAG_COLWIDTH_PATH = "col-width-path";
	private static final String TAG_COLWIDTH_NAME = "col-width-name";
	private static final String TAG_SEARCHVIEWSTATE = "tag-search-view-state";
	private String id;
	private Object uiState;
	private Composite control;
	private TagSearchResultsViewContentProvider viewContentProvider;
	private ISearchResult result;
	private TableViewer resultViewer;
	private TableColumn nameCol,pathCol,tagsCol;
	private int nameColWidth = 100,pathColWidth = 100,tagsColWidth = 100;
	private ISearchResultViewPart searchResultViewPart;
	private MenuManager menuManager;
	private SelectionProviderAdapter selectionProviderAdapter;
	
	@Override
	public Control getControl() {return(control);}

	@Override
	public void setFocus() {resultViewer.getTable().setFocus();}

	public void setID(String id) {this.id = id;}
	
	public String getID() {return(id);}

	public String getLabel() {return(TaggerMessages.TagSearchResultPage_Label);}

	public Object getUIState() {return(uiState);}

	public void saveState(IMemento memento){
		final IMemento mem = memento.createChild(TAG_SEARCHVIEWSTATE);
		mem.putInteger(TAG_COLWIDTH_NAME,nameCol.getWidth());
		mem.putInteger(TAG_COLWIDTH_PATH,pathCol.getWidth());
		mem.putInteger(TAG_COLWIDTH_TAGS,tagsCol.getWidth());
	}

	public void restoreState(IMemento memento) {
		if(memento != null){
			final IMemento mem = memento.getChild(TAG_SEARCHVIEWSTATE);
			if(mem != null){
				this.nameColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_NAME,100);
				this.pathColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_PATH,100);
				this.tagsColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_TAGS,100);
			}
		}
	}

	public void setInput(ISearchResult newSearch, Object uiState) {
		if(newSearch == null){
			((TagSearchResult)result).clearMatches();
			return;
		}

		this.result = newSearch;

		if(viewContentProvider == null){
			this.viewContentProvider = new TagSearchResultsViewContentProvider();
		}

		viewContentProvider.inputChanged(resultViewer,result,newSearch);

		// TODO: what to do with state
	}

	public void setViewPart(ISearchResultViewPart part) {this.searchResultViewPart = part;}

	@Override
	public void createControl(Composite parent) {
		// setup the context menu
		this.menuManager = new MenuManager("#PopUp");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.setParent(getSite().getActionBars().getMenuManager());
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				createContextMenuGroups(mgr);
				fillContextMenu(mgr);
				searchResultViewPart.fillContextMenu(mgr);
			}
		});
		
		selectionProviderAdapter = new SelectionProviderAdapter();
		getSite().setSelectionProvider(selectionProviderAdapter);
		
		getSite().registerContextMenu(searchResultViewPart.getViewSite().getId(), menuManager,selectionProviderAdapter);
		
		// build the page controls
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));

		resultViewer = new TableViewer(panel, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		resultViewer.setContentProvider(new TagSearchResultsViewContentProvider());
		resultViewer.setLabelProvider(new TagSearchResultsViewLabelProvider());
		
		final Table table = resultViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		table.setHeaderVisible(true);

		this.nameCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Name,nameColWidth);
		this.pathCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Path,pathColWidth);
		this.tagsCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Tags,tagsColWidth);

		resultViewer.setInput(null);

		resultViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if(!selection.isEmpty() && selection instanceof IStructuredSelection){
					final IStructuredSelection iss = (IStructuredSelection)selection;

					final OpenFileAction action = new OpenFileAction(getSite().getWorkbenchWindow().getActivePage());
					action.selectionChanged(iss);
					action.run();
				}
			}
		});

		resultViewer.addSelectionChangedListener(selectionProviderAdapter);
		
		this.control = panel;
	}
	
	@Override
	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		searchResultViewPart.fillContextMenu(actionBars.getMenuManager());
	}
	
	private void fillContextMenu(IMenuManager imm){
		final Action testAction = new Action(){
			@Override
			public void run() {
				System.out.println("I am running!");
			}
		};
		testAction.setId("fake.testAction");
		testAction.setImageDescriptor(TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.PLUGIN_ID, "icons/plus.gif"));
		testAction.setText("Testing");
		testAction.setToolTipText("This is a test...");
		imm.appendToGroup("additions", testAction);
		
		// NOTE: work related to getting the context menu to work
//		imm.appendToGroup(IContextMenuConstants.GROUP_REORGANIZE, fCopyToClipboardAction);
//		imm.appendToGroup(IContextMenuConstants.GROUP_SHOW, fShowNextAction);
//		imm.appendToGroup(IContextMenuConstants.GROUP_SHOW, fShowPreviousAction);
//		
//		if (getCurrentMatch() != null){
//			imm.appendToGroup(IContextMenuConstants.GROUP_REMOVE_MATCHES, fRemoveCurrentMatch);
//		}
//		
//		if (canRemoveMatchesWith(getViewer().getSelection())){
//			imm.appendToGroup(IContextMenuConstants.GROUP_REMOVE_MATCHES, fRemoveSelectedMatches);
//		}
//		
//		imm.appendToGroup(IContextMenuConstants.GROUP_REMOVE_MATCHES, fRemoveAllResultsAction);
	}
	
	private static void createContextMenuGroups(IMenuManager menu) {
		menu.add(new Separator(IContextMenuConstants.GROUP_NEW));
		menu.add(new GroupMarker(IContextMenuConstants.GROUP_GOTO));
		menu.add(new GroupMarker(IContextMenuConstants.GROUP_OPEN));
		menu.add(new Separator(IContextMenuConstants.GROUP_SHOW));
		menu.add(new Separator(IContextMenuConstants.GROUP_REMOVE_MATCHES));
		menu.add(new Separator(IContextMenuConstants.GROUP_REORGANIZE));
		menu.add(new GroupMarker(IContextMenuConstants.GROUP_GENERATE));
		menu.add(new Separator(IContextMenuConstants.GROUP_SEARCH));
		menu.add(new Separator(IContextMenuConstants.GROUP_BUILD));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IContextMenuConstants.GROUP_VIEWER_SETUP));
		menu.add(new Separator(IContextMenuConstants.GROUP_PROPERTIES));
	}

	private TableColumn createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
		return(col);
	}

	@Override
	public void dispose() {
		super.dispose();
		
		resultViewer.removeSelectionChangedListener(selectionProviderAdapter);
		
		viewContentProvider.dispose();
		control.dispose();
		
		getSite().setSelectionProvider(null);
	}
	
	private class SelectionProviderAdapter implements ISelectionProvider, ISelectionChangedListener {
		
		private List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>(5);
		
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			listeners.add(listener);
		}

		public ISelection getSelection() {
			return resultViewer.getSelection();
		}

		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			listeners.remove(listener);
		}

		public void setSelection(ISelection selection) {
			resultViewer.setSelection(selection);
		}

		public void selectionChanged(SelectionChangedEvent event) {
			SelectionChangedEvent wrappedEvent = new SelectionChangedEvent(this, event.getSelection());
			for(ISelectionChangedListener listener : listeners){
				listener.selectionChanged(wrappedEvent);
			}
		}

	}	
}
