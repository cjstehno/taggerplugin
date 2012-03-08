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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Search page that adds tag search support.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSearchPage extends DialogPage implements ISearchPage {

	private Table tagList;
	private TableItem[] tableItems;
	private Button requireAllBtn;
	private ISearchPageContainer searchPageContainer;

	public void setContainer(ISearchPageContainer container) {this.searchPageContainer = container;}

	public void createControl(Composite parent) {
		final Group panel = new Group(parent,SWT.SHADOW_ETCHED_IN);
		panel.setLayout(new GridLayout(1,false));
		panel.setText(TaggerMessages.TagSearchPage_Label_TagList);

		// list of tags
		tagList = new Table(panel,SWT.CHECK | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		tagList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		final Tag[] tags = TagManager.getInstance().getTags();
		Arrays.sort(tags);

		tableItems = new TableItem[tags.length];
		for (int t=0; t<tags.length; t++) {
			tableItems[t] = new TableItem(tagList,SWT.NONE);
			tableItems[t].setText(tags[t].getName());
			tableItems[t].setData(tags[t].getId());
		}

		// require all
		requireAllBtn = new Button(panel,SWT.CHECK);
		requireAllBtn.setText(TaggerMessages.TagSearchPage_Label_AllRequired);

		setControl(panel);
	}

	public boolean performAction() {
		try {
			NewSearchUI.runQueryInBackground(newQuery());
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(),TaggerMessages.TagSearchPage_Error_Title,TaggerMessages.bind(TaggerMessages.TagSearchPage_Error_Message, e.getMessage()), e.getStatus());
			return false;
		}
 		return true;
	}

	private ISearchQuery newQuery() throws CoreException {
		final TagSearchInput input = new TagSearchInput(extractSelectedTagIds(),requireAllBtn.getSelection(),searchPageContainer);
		final TagSearchResult result = new TagSearchResult();
		final ISearchQuery query = new TagSearchQuery(input,result);
		return(query);
	}

	private String[] extractSelectedTagIds(){
		final List<String> ids = new LinkedList<String>();
		for(TableItem item : tableItems){
			if(item.getChecked()){
				ids.add((String)item.getData());
			}
		}
		return(ids.toArray(new String[ids.size()]));
	}
}
