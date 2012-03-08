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
package net.sourceforge.taggerplugin.ui;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggedMarker;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page addition to resources that displays a list of all tags associated with the resource.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggableResourcePropertyPage extends PropertyPage {

	private static final String LIST_SEPARATOR = ", ";

	@Override
	protected Control createContents(Composite parent) {
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));

		final Label tagsLbl = new Label(panel,SWT.LEFT);
		tagsLbl.setFont(parent.getFont());
		tagsLbl.setText(TaggerMessages.TaggableResourcePropertyPage_Label_Associations);

		final Text tagsTxt = new Text(panel,SWT.MULTI | SWT.WRAP);
		tagsTxt.setEditable(false);
		tagsTxt.setText(extractTagAssociations());

		return(panel);
	}

	/**
	 * Used to extract the tag associations from the resource and return the list of
	 * tag names as a comma-separated string.
	 *
	 * @return a comma-separated list of tagnames associated with the selected resource.
	 */
	private String extractTagAssociations(){
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker((IResource)getElement().getAdapter(IResource.class));
			if(marker != null){
				final Tag[] tags = TagManager.getInstance().findTags(marker.getTaggableResource().listTags());
				if(tags.length > 0){
					return(TagManager.extractTagNames(tags,LIST_SEPARATOR));
				}	
			}
		} catch(Exception tae){
			ExceptionDialogFactory.create(getShell(),tae).open();
		}
		return(TaggerMessages.TaggableResourcePropertyPage_NoAssociations);
	}
}
