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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.io.TagAssociationIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;
import net.sourceforge.taggerplugin.util.IoUtils;
import net.sourceforge.taggerplugin.util.SynchWithDisplay;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IImportWizard;

/**
 * Wizard for the importing of tag association data.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationImportWizard extends AbstractTagAssociationExternalizationWizard implements IImportWizard {

	/**
	 * Creates a new tag import wizard.
	 */
	public TagAssociationImportWizard(){
		super(TagAssociationExternalizationWizardType.IMPORT);
	}

	/**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
	protected void doFinish(final IProgressMonitor monitor, final File file, final TagIoFormat format) throws CoreException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			final Map<String,TagAssociation> associations = TagAssociationIoFactory.create(format).readTagAssociations(reader, monitor);

			SynchWithDisplay.synch(new Runnable(){
				public void run() {
					final TagAssociationManager mgr = TagAssociationManager.getInstance();
					for(TagAssociation assoc : associations.values()){
						for(String tid : assoc.getAssociations()){
							if(!mgr.associationExists(assoc.getResourceId(), tid)){
								try {
									mgr.addAssociation(TaggedMarkerHelper.getResource(assoc.getResourceId()), tid);
								} catch(CoreException ce){
									TaggerLog.error(ce);
								}
							}	
						}
					}
				}
			});

		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(reader);
		}
	}
}
