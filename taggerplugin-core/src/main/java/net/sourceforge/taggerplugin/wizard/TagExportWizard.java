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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.io.TagIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IExportWizard;

/**
 * Wizard used to export the available tag set to a file on the filesystem.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagExportWizard extends AbstractTagExternalizationWizard implements IExportWizard {

	public TagExportWizard(){
		super(TagExternalizationWizardType.EXPORT);
	}

	/**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
	protected void doFinish(final IProgressMonitor monitor, final File exportFile, final TagIoFormat exportFormat) throws CoreException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(exportFile));
			TagIoFactory.create(exportFormat).writeTags(writer, TagManager.getInstance().getTags(), monitor);
		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}
}
