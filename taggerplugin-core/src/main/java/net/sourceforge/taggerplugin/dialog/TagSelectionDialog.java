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

import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;

/**
 * Dialog used to select one or more tags using check-boxes.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSelectionDialog extends ListSelectionDialog {

	/**
	 * Creates a new tag selection dialog.
	 * 
	 * @param parentShell parent shell to use
	 * @param tags the tags to display
	 * @param title the dialog title
	 * @param text the dialog text
	 */
	public TagSelectionDialog(Shell parentShell, Tag[] tags, String title, String text){
		super(parentShell,tags, new TagListContentProvider(),new TagLabelProvider(),text);
		setTitle(title);
	}

	/**
	 * Content provider for the tag selection dialog.
	 *
	 * @author Christopher J. Stehno (chris@stehno.com)
	 */
	private static final class TagListContentProvider implements IStructuredContentProvider {

		private Tag[] input;

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return(input);
		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose(){}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.input = (Tag[])newInput;
		}
	}

	/**
	 * Label provider for the tag selection dialog.
	 *
	 * @author Christopher J. Stehno (chris@stehno.com)
	 */
	private static final class TagLabelProvider implements ILabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			return(((Tag)element).getName());
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {}
	}
}
