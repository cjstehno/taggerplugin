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
package net.sourceforge.taggerplugin;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.manager.TagManager;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Base plug-in class that controls the plug-in life cycle.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "net.sourceforge.taggerplugin";
	private static TaggerActivator plugin;

	public TaggerActivator() {
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		TagAssociationManager tagAssocMgr = TagAssociationManager.getInstance();
		TagManager.getInstance().addTagManagerListener(tagAssocMgr);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(tagAssocMgr, IResourceChangeEvent.POST_CHANGE);
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		TagAssociationManager tagAssocMgr = TagAssociationManager.getInstance();
		TagManager.getInstance().removeTagManagerListener(tagAssocMgr);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(tagAssocMgr);

		// automatically persist the tag set when plugin is stopped
		TagManager.getInstance().saveTags();
		TagAssociationManager.getInstance().saveAssociations();

		plugin = null;
	}

	/**
	 * Returns the shared instance for this plug-in.
	 *
	 * @return the shared instance
	 */
	public static TaggerActivator getDefault() {
		return plugin;
	}
}
