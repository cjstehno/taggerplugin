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
package net.sourceforge.taggerplugin.wizard;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.util.StringUtils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Tag externalization wizard page used by both the import and export wizard. 
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagAssociationExternalizationWizardPage extends WizardPage {

	private static final String EMPTY_STRING = "";
	private Text filePath;
	private Button xmlFormatBtn,csvFormatBtn;
	private final TagAssociationExternalizationWizardType descriptor;
	
	TagAssociationExternalizationWizardPage(final TagAssociationExternalizationWizardType descriptor){
		super(descriptor.getPageId(),descriptor.equals(TagExternalizationWizardType.IMPORT) ? TaggerMessages.TagImportWizardPage_Title : TaggerMessages.TagExportWizardPage_Title,null);
		this.descriptor = descriptor;
	}

	/**
	 * Used to retrieve the file path selected by the user.
	 *
	 * @return the selected file path.
	 */
	String getFilePath(){return(filePath.getText());}

	/**
	 * Used to retrieve the import/export file format selected by the user.
	 *
	 * @return the import/export file format
	 */
	TagIoFormat getTagFormat(){
		if(xmlFormatBtn.getSelection()){
			return((TagIoFormat)xmlFormatBtn.getData());
		} else if(csvFormatBtn.getSelection()){
			return((TagIoFormat)csvFormatBtn.getData());
		} else {
			return(null);
		}
	}

	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));

		// path
		createLabel(panel,TaggerMessages.TagIoWizardPage_Label_File,descriptor.equals(TagAssociationExternalizationWizardType.IMPORT) ? TaggerMessages.TagImportWizardPage_Tooltip_File : TaggerMessages.TagExportWizardPage_Tooltip_File,1);

		filePath = new Text(panel,SWT.BORDER | SWT.SINGLE);
		filePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filePath.setEditable(false);
		filePath.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){dialogChanged();}
		});

		final Button directoryBtn = new Button(panel,SWT.PUSH);
		directoryBtn.setText(TaggerMessages.TagIoWizardPage_Button_Browse);
		directoryBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				final FileDialog dialog = new FileDialog(getShell(),SWT.SAVE);
				final String path = dialog.open();
				filePath.setText(path != null ? path : EMPTY_STRING);
			}
		});

		// export format
		createLabel(panel,TaggerMessages.TagIoWizardPage_Label_Format,TaggerMessages.TagIoWizardPage_Tooltip_Format,3);

		xmlFormatBtn = new Button(panel,SWT.RADIO);
		xmlFormatBtn.setLayoutData(createGridData(3));
		xmlFormatBtn.setText(TaggerMessages.TagIoWizardPage_Format_Xml);
		xmlFormatBtn.setData(TagIoFormat.XML);
		xmlFormatBtn.setSelection(true);

		csvFormatBtn = new Button(panel,SWT.RADIO);
		csvFormatBtn.setLayoutData(createGridData(3));
		csvFormatBtn.setText(TaggerMessages.TagIoWizardPage_Format_Csv);
		csvFormatBtn.setData(TagIoFormat.CSV);

		dialogChanged();
		setControl(panel);
	}

	private void dialogChanged(){
		if(StringUtils.isBlank(filePath.getText())){
			updateStatus(TaggerMessages.TagIoWizardPage_Error_NoFile);
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private Label createLabel(Composite container, String text, String tip, int hspan){
		final Label lbl = new Label(container,SWT.NULL);
		lbl.setText(text);
		lbl.setToolTipText(tip);
		lbl.setLayoutData(createGridData(hspan));
		return(lbl);
	}

	private GridData createGridData(int hspan){
		final GridData gd = new GridData();
		gd.horizontalSpan = hspan;
		return(gd);
	}
}
