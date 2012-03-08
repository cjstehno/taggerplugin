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
package net.sourceforge.taggerplugin.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.event.ITagAssociationManagerListener;
import net.sourceforge.taggerplugin.event.ITagManagerListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.event.TagManagerEvent;
import net.sourceforge.taggerplugin.io.TagAssociationIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.resource.ITaggable;
import net.sourceforge.taggerplugin.resource.ITaggedMarker;
import net.sourceforge.taggerplugin.resource.RemovedResourceDeltaVisitor;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;
import net.sourceforge.taggerplugin.search.ITagSearchResult;
import net.sourceforge.taggerplugin.search.TagSearchResult;
import net.sourceforge.taggerplugin.search.TaggableResourceVisitor;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * Manager used to load and store the tag/resource associations in the workspace.
 * The tag/resource associations are stored as part of the plugin state.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationManager implements IResourceChangeListener,ITagManagerListener {

	private static final String TAGASSOCIATIONFILENAME = "tag-associations.xml";
	private static TagAssociationManager instance;
	private Map<String, TagAssociation> associations;
	private final List<ITagAssociationManagerListener> listeners;

	private TagAssociationManager(){
		super();
		this.listeners = new LinkedList<ITagAssociationManagerListener>();
	}

	public static final TagAssociationManager getInstance(){
		if(instance == null){
			instance = new TagAssociationManager();
		}
		return(instance);
	}

	/**
	 * Used to find all associations (tag ids) that are shared by all of the resources with
	 * the given ids. An association will ONLY appear in the resulting set if it is shared by
	 * all resources in the id list.
	 *
	 * @param resourceIds
	 * @return
	 */
	public Set<String> findSharedAssociations(ITaggable[] taggables){
		ensureAssociations();

		final Set<String> shared = new HashSet<String>();
		try {
			final Set<String> masterSet = new HashSet<String>();
			for (ITaggable taggable : taggables){
				final ITaggedMarker marker = TaggedMarkerHelper.getMarker(taggable.getResource());
				if(marker != null){
					final TagAssociation assocs = associations.get(marker.getResourceId());
					if(assocs != null && assocs.hasAssociations()){
						masterSet.addAll(Arrays.asList(assocs.getAssociations()));
					}
				}
			}

			for(String uuid : masterSet){
				boolean allhave = false;
				for(ITaggable taggable : taggables){
					final ITaggedMarker marker = TaggedMarkerHelper.getMarker(taggable.getResource());
					if(marker != null){
						final TagAssociation assocs = associations.get(marker.getResourceId());
						if(assocs != null && assocs.hasAssociations()){
							allhave = assocs.containsAssociation(uuid);
							if(!allhave){
								break;
							}
						} else {
							allhave = false;
							break;
						}
					} else {
						break;
					}
				}

				if(allhave){
					shared.add(uuid);
				}
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to find associations: " + ce.getMessage(),ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_List,ce);
		}
		return(shared);
	}

	public Set<String> findAllAssociations(ITaggable[] taggables){
		final Set<String> set = new HashSet<String>();
		for(ITaggable taggable : taggables){
			set.addAll(Arrays.asList(taggable.listTags()));
		}
		return(set);
	}


	/**
	 * Used to clear all associations of the given resource.
	 *
	 * @param resource the resource
	 */
	public void clearAssociations(IResource resource){
		ensureAssociations();
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation tags = associations.remove(marker.getResourceId());
				if(tags != null){
					TaggedMarkerHelper.deleteMarker(resource);
					fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.REMOVED,resource));
					saveAssociations();
				}
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to clear tag associations: " + ce.getMessage(), ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Remove,ce);
		}
	}

	/**
	 * does not save the state
	 *
	 * @param resource
	 * @param tagIds
	 */
	public void clearAssociations(IResource resource, String[] tagIds){
		ensureAssociations();
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation assoc = associations.get(marker.getResourceId());
				if(assoc == null){
					// cleanup bad marker (should be no marker for resource without tag associations)
					TaggedMarkerHelper.deleteMarker(resource);
				} else {
					if(assoc.removeAssociations(tagIds) && assoc.isEmpty()){
						TaggedMarkerHelper.deleteMarker(resource);
						fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.REMOVED,resource));
					}
				}
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to clear tag associations: " + ce.getMessage(), ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Remove,ce);
		}
	}

	/**
	 * This method should only be called by the resource event listener. It does not handle the
	 * marker removal (as the resource deletion should take care of it. This method also does not
	 * fire any TagAssociationEvent.
	 *
	 * @param resourceId the id of the resource being deleted
	 */
	public void deleteAssociations(String resourceId){
		ensureAssociations();

		associations.remove(resourceId);

		saveAssociations();
	}

	/**
	 * Used to clear (remove) the association of the tag with the given id
	 * with the specified resource.
	 *
	 * @param resource the resource
	 * @param tagid the id of the tag association being cleared
	 */
	public void clearAssociation(IResource resource, String tagid){
		ensureAssociations();

		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation tags = associations.get(marker.getResourceId());
				if(tags != null && tags.hasAssociations()){
					tags.removeAssociation(tagid);

					if(tags.isEmpty()){
						// there are no more associations, clear the marker
						TaggedMarkerHelper.deleteMarker(resource);
					}

					fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.REMOVED,resource));
					saveAssociations();
				}
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to clear tag association: " + ce.getMessage(), ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Remove,ce);
		}
	}

	public boolean hasAssociation(IResource resource, String tagid){
		ensureAssociations();

		boolean hasAssoc = false;
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation tags = associations.get(marker.getResourceId());
				hasAssoc = tags != null && tags.hasAssociations() && tags.containsAssociation(tagid);
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to determine association: " + ce.getMessage(), ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Has,ce);
		}
		return(hasAssoc);
	}

	public boolean hasAssociations(IResource resource){
		ensureAssociations();
		boolean hasAssoc = false;
		try {
			hasAssoc = TaggedMarkerHelper.getMarker(resource) != null;
		} catch(CoreException ce){
			TaggerLog.error("Unable to determine association: " + ce.getMessage(), ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Has,ce);
		}
		return(hasAssoc);
	}
	
	public boolean associationExists(String resourceId, String tagId){
		ensureAssociations();
		final TagAssociation assoc = associations.get(resourceId);
		return(assoc != null && assoc.containsAssociation(tagId));
	}

	/**
	 * Used to associate the tag with the specified id to the given resource.
	 *
	 * @param resource the resource
	 * @param tagid the id of the tag being associated with the resource
	 */
	public void addAssociation(IResource resource, String tagid){
		ensureAssociations();
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getOrCreateMarker(resource);
			if(marker != null){
				final TagAssociation tags = getOrCreateAssociation(marker.getResourceId());
				tags.addTagId(tagid);

				fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.ADDED,resource));
				saveAssociations();
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to create tag association: " + ce.getMessage(),ce);
			throw new TagAssociationException(TaggerMessages.TagAssociationManager_Error_Create,ce);
		}
	}
	
	public Map<String,TagAssociation> getAssociationMap(){
		ensureAssociations();
		return(associations);
	}

	public String[] getAssociations(IResource resource){
		ensureAssociations();
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				return(associations.get(marker.getResourceId()).getAssociations());
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to retrive association: " + ce.getMessage(), ce);
			throw new TagAssociationException("",ce);
		}
		return(new String[0]);
	}

	private void ensureAssociations(){
		if(associations == null){
			loadAssociations();
		}
	}

	/**
	 * Used to retrieve the set of tag ids associated with the given resource id. If the set
	 * does not exist, one will be created, added to the associations map, and returned
	 * for use.
	 *
	 * @param resourceId
	 * @return
	 */
	private TagAssociation getOrCreateAssociation(String resourceId){
		ensureAssociations();
		
		TagAssociation tagAssoc = associations.get(resourceId);
		if(tagAssoc == null){
			tagAssoc = new TagAssociation(resourceId);
			associations.put(resourceId, tagAssoc);
		}
		return(tagAssoc);
	}

	private void loadAssociations(){
		final File file = getTagAssociationFile();
		if(file.exists()){
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				associations = TagAssociationIoFactory.create(TagIoFormat.MEMENTO).readTagAssociations(reader, null);
			} catch(Exception ex){
				TaggerLog.error(ex);
			} finally {
				IoUtils.closeQuietly(reader);
			}
		} else {
			associations = new HashMap<String, TagAssociation>();
		}
	}

	public void saveAssociations(){
		if(associations == null){return;}

		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(getTagAssociationFile()));
			TagAssociationIoFactory.create(TagIoFormat.MEMENTO).writeTagAssociations(writer,associations, null);
		} catch (Exception e) {
			TaggerLog.error(e);
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		try {
	         switch (event.getType()) {
	            case IResourceChangeEvent.PRE_CLOSE:
	            	break;
	            case IResourceChangeEvent.PRE_DELETE:
	            	event.getDelta().accept(new RemovedResourceDeltaVisitor());
	            	break;
	            default:
	            	break;
	         }
		} catch(CoreException ce){
			TaggerLog.error("Unable to handle resource change event: " + ce.getMessage(), ce);
		}
	}

	public void handleTagManagerEvent(TagManagerEvent tme) {
		if(tme.getType().equals(TagManagerEvent.Type.REMOVED)){
			try {
				// find all associations that contain the removed tag and remove that tagid
				final String[] removedTagIds = TagManager.extractTagIds(tme.getTags());

				final ITagSearchResult result = new TagSearchResult();
				ResourcesPlugin.getWorkspace().getRoot().accept(new TaggableResourceVisitor(removedTagIds,false,result), IResource.NONE);
				for(IResource resource : result.getMatches()){
					clearAssociations(resource,removedTagIds);
				}

				saveAssociations();
			} catch(CoreException ce){
				TaggerLog.error("Unable to handler tag removal event: " + ce.getMessage(),ce);
			}
		}
	}

	/**
	 * Used to retrieve the file in the plugin state directory used to store the tag association
	 * information.
	 *
	 * @return the tag association persistance file
	 */
	private File getTagAssociationFile() {
		return(TaggerActivator.getDefault().getStateLocation().append(TAGASSOCIATIONFILENAME).toFile());
	}

	public void addTagAssociationListener(ITagAssociationManagerListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	public void removeTagAssociationListener(ITagAssociationManagerListener listener){
		listeners.remove(listener);
	}

	private void fireTagAssociationEvent(TagAssociationEvent event){
		for(ITagAssociationManagerListener listener : listeners){
			listener.handleTagAssociationEvent(event);
		}
	}
}
