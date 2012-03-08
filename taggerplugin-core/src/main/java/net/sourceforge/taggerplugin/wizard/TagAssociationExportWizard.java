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
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.io.TagAssociationIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IExportWizard;

public class TagAssociationExportWizard extends AbstractTagAssociationExternalizationWizard implements IExportWizard {

	public TagAssociationExportWizard(){
		super(TagAssociationExternalizationWizardType.EXPORT);
	}

	/**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
	protected void doFinish(final IProgressMonitor monitor, final File exportFile, final TagIoFormat exportFormat) throws CoreException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(exportFile));
			TagAssociationIoFactory.create(exportFormat).writeTagAssociations(writer, TagAssociationManager.getInstance().getAssociationMap(), monitor);
		} catch(Exception ex){
			TaggerLog.error(ex);
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}
}
