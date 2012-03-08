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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

/**
 * Base wizard class for the tag externalization wizards (import/export).
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
abstract class AbstractTagExternalizationWizard extends Wizard {

	private TagExternalizationWizardPage page;
	private final TagExternalizationWizardType wizardType;

	protected AbstractTagExternalizationWizard(TagExternalizationWizardType type){
		super();
		this.wizardType = type;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {/* nothing */}

	/**
	 * @see Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.page = new TagExternalizationWizardPage(wizardType);
		addPage(page);
	}

	/**
	 * @see Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final File file = new File(page.getFilePath());
		final TagIoFormat format = page.getTagFormat();

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor,file,format);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			ExceptionDialogFactory.create(getShell(), e.getTargetException()).open();
			return false;
		}
		return true;
	}

	/**
	 * Performs the actual finishing step action of the externalizer (import/export).
	 *
	 * @param monitor the progress monitor
	 * @param file the target file
	 * @param format the target file format
	 * @throws CoreException if there is a problem finishing the wizard.
	 */
	protected abstract void doFinish(IProgressMonitor monitor, File file, TagIoFormat format) throws CoreException;
}
