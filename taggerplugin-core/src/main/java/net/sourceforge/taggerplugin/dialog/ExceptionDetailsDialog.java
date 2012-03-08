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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Dictionary;

import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 	Dialog for displaying error messages to the user with additional details information available by clicking the
 * 	"Show Details" button. Generally the details will be the stack trace from the exception.
 * 
 * 	Note: This original source of this code was taken from the book "Eclipse: Building Commercial-Quality Plug-ins"
 * 	which is provided under the Eclipse Common Public License.
 * 
 *	@author Eric Clayberg (qualityeclipse.org)
 *	@author Dan Rubel (qualityeclipse.org)
 *	@author Christopher J. Stehno (chris@stehno.com)
 */
public class ExceptionDetailsDialog extends AbstractDetailsDialog {

	private static final String BUNDLE_VERSION = "Bundle-Version";
	private static final String BUNDLE_NAME = "Bundle-Name";
	private static final String BUNDLE_VENDOR = "Bundle-Vendor";
	private final Object details;
	private final Plugin plugin;

	/**
	 * Construct a new instance with the specified elements. Note that the window will have no 
	 * visual representation (no widgets) until it is told to open. By default, <code>open</code> 
	 * blocks for dialogs.
	 * 
	 * @param parentShell the parent shell, or <code>null</code> to create a top-level shell
	 * @param title the title for the dialog or <code>null</code> for none
	 * @param image the image to be displayed
	 * @param message the message to be displayed
	 * @param details an object whose content is to be displayed in the details area, or <code>null</code> for none
	 * @param plugin The plugin triggering this deatils dialog and whose information is to be shown in the details area or <code>null</code> if no plugin details should be shown.
	 */
	public ExceptionDetailsDialog(Shell parentShell, String title,Image image, String message, Object details, Plugin plugin){
		super(parentShell, getTitle(title, details), getImage(image,details), getMessage(message, details));

		this.details = details;
		this.plugin = plugin;
	}

	/**
	 * Build content for the area of the dialog made visible when the Details button is clicked.
	 * 
	 * @param parent the details area parent
	 * @return the details area
	 */
	protected Control createDetailsArea(Composite parent) {
		// Create the details area.
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		panel.setLayout(layout);

		// Create the details content.
		createProductInfoArea(panel);
		createDetailsViewer(panel);

		return panel;
	}

	/**
	 * Create fields displaying the plugin information such as name, identifer, version and vendor. Do nothing if the plugin is not
	 * specified.
	 * 
	 * @param parent the details area in which the fields are created
	 * @return the product info composite or <code>null</code> if no plugin specified.
	 */
	protected Composite createProductInfoArea(Composite parent) {
		// If no plugin specified, then nothing to display here
		if (plugin == null){return null;}

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayout(layout);

		final Dictionary bundleHeaders = plugin.getBundle().getHeaders();

		new Label(composite, SWT.NONE).setText(TaggerMessages.ExceptionDetailsDialog_Label_Provider);
		new Label(composite, SWT.NONE).setText((String)bundleHeaders.get(BUNDLE_VENDOR));
		new Label(composite, SWT.NONE).setText(TaggerMessages.ExceptionDetailsDialog_Label_PluginName);
		new Label(composite, SWT.NONE).setText((String)bundleHeaders.get(BUNDLE_NAME));
		new Label(composite, SWT.NONE).setText(TaggerMessages.ExceptionDetailsDialog_Label_PluginId);
		new Label(composite, SWT.NONE).setText(plugin.getBundle().getSymbolicName());
		new Label(composite, SWT.NONE).setText(TaggerMessages.ExceptionDetailsDialog_Label_Version);
		new Label(composite, SWT.NONE).setText((String)bundleHeaders.get(BUNDLE_VERSION));

		return composite;
	}

	/**
	 * Create the details field based upon the details object. Do nothing if the details object is not specified.
	 * 
	 * @param parent the details area in which the fields are created
	 * @return the details field
	 */
	protected Control createDetailsViewer(Composite parent) {
		if (details == null){return null;}

		Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Create the content.
		StringWriter writer = new StringWriter(1000);
		if (details instanceof Throwable){
			appendException(new PrintWriter(writer), (Throwable) details);
		} else if (details instanceof IStatus){
			appendStatus(new PrintWriter(writer), (IStatus) details, 0);
		}
		text.setText(writer.toString());

		return text;
	}

	/**
	 * Answer the title based on the provided title and details object.
	 */
	public static String getTitle(String title, Object details) {
		if (title != null){return title;}
		
		if (details instanceof Throwable) {
			Throwable e = (Throwable) details;
			while (e instanceof InvocationTargetException){
				e = ((InvocationTargetException) e).getTargetException();
			}
			String name = e.getClass().getName();
			return name.substring(name.lastIndexOf('.') + 1);
		}
		return "Exception";
	}

	/**
	 * Answer the image based on the provided image and details object.
	 */
	public static Image getImage(Image image, Object details) {
		if (image != null){return image;}
		
		Display display = Display.getCurrent();
		if (details instanceof IStatus) {
			switch (((IStatus) details).getSeverity()) {
				case IStatus.ERROR:
					return display.getSystemImage(SWT.ICON_ERROR);
				case IStatus.WARNING:
					return display.getSystemImage(SWT.ICON_WARNING);
				case IStatus.INFO:
					return display.getSystemImage(SWT.ICON_INFORMATION);
				case IStatus.OK:
					return null;
			}
		}
		return display.getSystemImage(SWT.ICON_ERROR);
	}

	/**
	 * Answer the message based on the provided message and details object.
	 */
	public static String getMessage(String message, Object details) {
		if (details instanceof Throwable) {
			Throwable e = (Throwable) details;
			while (e instanceof InvocationTargetException){
				e = ((InvocationTargetException) e).getTargetException();
			}
			
			if (message == null){
				return e.toString();
			}
			
			return MessageFormat.format(message, new Object[] { e.toString() });
		}
		
		if (details instanceof IStatus) {
			String statusMessage = ((IStatus) details).getMessage();
			if (message == null){
				return statusMessage;
			}
			
			return MessageFormat.format(message,new Object[] { statusMessage });
		}
		
		if (message != null){
			return message;
		}
		
		return "An Exception occurred.";
	}

	public static void appendException(PrintWriter writer, Throwable ex){
		if (ex instanceof CoreException) {
			appendStatus(writer, ((CoreException) ex).getStatus(), 0);
			writer.println();
		}
		
		appendStackTrace(writer, ex);
		
		if (ex instanceof InvocationTargetException){
			appendException(writer, ((InvocationTargetException) ex).getTargetException());
		}
	}

	public static void appendStatus(PrintWriter writer, IStatus status,int nesting){
		for (int i = 0; i < nesting; i++){
			writer.print("  ");
		}
		
		writer.println(status.getMessage());
		IStatus[] children = status.getChildren();
		
		for (int i = 0; i < children.length; i++){
			appendStatus(writer, children[i], nesting + 1);
		}
	}

	public static void appendStackTrace(PrintWriter writer, Throwable ex){
		ex.printStackTrace(writer);
	}
}
