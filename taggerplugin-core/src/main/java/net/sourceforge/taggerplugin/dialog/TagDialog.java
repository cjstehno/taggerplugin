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
package net.sourceforge.taggerplugin.dialog;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for creating/editing tag data.
 *
 * Editing:
 * <pre>
 * 	TagDialog dia = new TagDialog(shell);
 * 	dia.setTag(theTag);
 * 	if(dia.showModify() == TagDialog.OK){
 * 		// the tag object will be updated with the new data
 * 	}
 * </pre>
 *
 * Creating:
 * <pre>
 * 	TagDialog dia = new TagDialog(shell);
 * 	if(dia.showCreate() == TagDialog.OK){
 * 		// a new tag object will be created
 * 		Tag newTag = dia.getTag();
 * 	}
 * </pre>
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagDialog extends Dialog {

    private String title,errorMessage;
    private Tag tag;
    private Text nameTxt,descTxt,errorMessageText;

    /**
     * Creates a new tag dialog using the given parent shell.
     * 
     * @param parentShell the parent shell
     */
    public TagDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Used to show the tag creation dialog.
     *
     * @return the button id
     */
    public int showCreate(){
    	this.title = TaggerMessages.TagDialog_Title_Create;
    	return(open());
    }

    /**
     * Used to show the tag modification dialog.
     *
     * @return the button id.
     */
    public int showModify(){
    	this.title = TaggerMessages.TagDialog_Title_Modify;
    	return(open());
    }

    /**
     * Used to specify the tag data.
     *
     * @param tag the tag data
     */
    public void setTag(Tag tag) {
		this.tag = tag;
	}

    /**
     * Used to retrive the tag data.
     *
     * @return the tag data
     */
    public Tag getTag() {
		return this.tag;
	}

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null){
        	shell.setText(title);
        }
    }

    /**
     *	@see Dialog#buttonPressed(int)
     */
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
        	if(tag == null){
        		tag = new Tag(nameTxt.getText(),descTxt.getText());
        	} else {
        		tag.setName(nameTxt.getText());
        		tag.setDescription(descTxt.getText());
        	}
        } else {
            tag = null;
        }
        super.buttonPressed(buttonId);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons
        createButton(parent, IDialogConstants.OK_ID,IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);

        nameTxt.setFocus();

        if(tag != null){
        	// set the edit data
        	nameTxt.setText(tag.getName());
        	descTxt.setText(tag.getDescription());
        }
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#eateDialogArea(Composite)
     */
    protected Control createDialogArea(Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        final GridLayout layout = (GridLayout)composite.getLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        final Font labelFont = parent.getFont();

        createNameLabel(composite, labelFont);
        createNameText(composite);
        createDescriptionLabel(composite, labelFont);
        createDescriptionText(composite);

        createErrorMessageText(composite);
        setErrorMessage(errorMessage);

        applyDialogFont(composite);
        return composite;
    }

    /**
     * Used to create the error message text area.
     *
     * @param composite the working composite
     */
	private void createErrorMessageText(final Composite composite) {
		errorMessageText = new Text(composite, SWT.READ_ONLY);
        final GridData errData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        errData.horizontalSpan = 2;
        errorMessageText.setLayoutData(errData);
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        errorMessageText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
	}

	/**
	 * Used to create the description text box.
	 *
	 * @param composite the working composite
	 */
	private void createDescriptionText(final Composite composite) {
		descTxt = new Text(composite,SWT.MULTI | SWT.WRAP | SWT.BORDER);
        descTxt.setTextLimit(250);
        final GridData descData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        descData.widthHint = 200;
        descData.heightHint = 100;
        descTxt.setLayoutData(descData);
        descTxt.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){
		        validate(descTxt,TaggerMessages.TagDialog_Error_NoDescription);
			}
        });
        descTxt.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e){}
			public void focusLost(FocusEvent e) {
				validate(descTxt,TaggerMessages.TagDialog_Error_NoDescription);
			}
        });
	}

	/**
	 * Used to create the description label.
	 *
	 * @param composite the working composite
	 * @param labelFont the label font
	 */
	private void createDescriptionLabel(final Composite composite, final Font labelFont) {
		final Label descLbl = new Label(composite,SWT.LEFT);
        descLbl.setText(TaggerMessages.TagDialog_Label_Description);
        descLbl.setFont(labelFont);
        descLbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
	}

	/**
	 * Used to create the name text box.
	 *
	 * @param composite the working composite
	 */
	private void createNameText(final Composite composite) {
		nameTxt = new Text(composite,SWT.SINGLE | SWT.BORDER);
        nameTxt.setTextLimit(25);
        final GridData nameData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        nameData.widthHint = 200;
        nameTxt.setLayoutData(nameData);
        nameTxt.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){
		        validate(nameTxt,TaggerMessages.TagDialog_Error_NoName);
			}
        });
        nameTxt.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e){}
			public void focusLost(FocusEvent e) {
				validate(nameTxt,TaggerMessages.TagDialog_Error_NoName);
			}
        });
	}

	/**
	 * Used to create the name label.
	 *
	 * @param composite the working composite
	 * @param labelFont the label font
	 */
	private void createNameLabel(final Composite composite, final Font labelFont) {
		final Label nameLbl = new Label(composite,SWT.LEFT);
        nameLbl.setText(TaggerMessages.TagDialog_Label_Name);
        nameLbl.setFont(labelFont);
	}

    /**
     * Used to validate the given text input. If the validation fails, the given
     * error message will be displayed.
     *
     * @param txt the text input to be validated
     * @param errMsg the error message to use if the input is invalid
     */
    private void validate(final Text txt, final String errMsg){
    	setErrorMessage(new TextInputValidator(errMsg).isValid(txt.getText()));
    }

    /**
     * Sets or clears the error message. If not <code>null</code>, the OK button is disabled.
     *
     * @param errorMessage the error message, or <code>null</code> to clear
     */
    public void setErrorMessage(String errorMessage) {
    	this.errorMessage = errorMessage;
    	if (errorMessageText != null && !errorMessageText.isDisposed()) {
    		errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
    		errorMessageText.getParent().update();
    		Control button = getButton(IDialogConstants.OK_ID);
    		if (button != null) {
    			button.setEnabled(errorMessage == null);
    		}
    	}
    }

    /**
     * InputValidator implementation used to validate the text inputs. The input is considered valid
     * if the text is not null and not empty.
     *
     * @author Christopher J. Stehno (chris@stehno.com)
     */
    private static final class TextInputValidator implements IInputValidator {

    	private final String msg;

    	/**
    	 * Creates a new input validator with the given error message.
    	 * 
    	 * @param msg the error message
    	 */
    	private TextInputValidator(final String msg){
    		super();
    		this.msg = msg;
    	}

		/**
		 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
		 */
		public String isValid(String newText){
			return(newText != null && newText.length() > 0 ? null : msg);
		}
    }
}
