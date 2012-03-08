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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.event.ITagManagerListener;
import net.sourceforge.taggerplugin.event.TagManagerEvent;
import net.sourceforge.taggerplugin.io.TagIoFactory;
import net.sourceforge.taggerplugin.io.TagIoFormat;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.util.IoUtils;

/**
 * Manages the tag set available to the plugin.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagManager {

	private static final String TAGSETFILENAME = "tags.xml";
	private static TagManager instance;
	private List<ITagManagerListener> listeners;
	private Map<String,Tag> tags;

	private TagManager(){
		super();
		this.listeners = new LinkedList<ITagManagerListener>();
	}

	/**
	 * Used to retrieve the shared instance of the TagManager.
	 *
	 * @return the shared instance of the TagManager
	 */
	public static TagManager getInstance(){
		if(instance == null){instance = new TagManager();}
		return(instance);
	}

	/**
	 * Used to find the the tags with the given set of tag ids. If an
	 * id does not map to a tag, it is ignored.
	 *
	 * @param tagids the tag ids to find tags for
	 * @return the array of tags found with the given ids.
	 */
	public Tag[] findTags(String[] tagids){
		ensureTags();

		List<Tag> list = new LinkedList<Tag>();
		for (String uuid : tagids) {
			final Tag tag = tags.get(uuid);
			if(tag != null){
				list.add(tag);
			}
		}
		return(list.toArray(new Tag[list.size()]));
	}

	/**
	 * Used to find all tags that do not have one of the specified tag ids.
	 *
	 * @param tagids the tag ids
	 * @return the tags not having the given ids.
	 */
	public Tag[] findTagsNotIn(String[] tagids){
		ensureTags();

		final List<String> ids = new ArrayList<String>(tags.keySet());
		ids.removeAll(Arrays.asList(tagids));

		final Tag[] matching = new Tag[ids.size()];
		for(int i=0; i<ids.size(); i++){
			matching[i] = tags.get(ids.get(i));
		}
		return(matching);
	}

	/**
	 * Used to add a new tag to the tag set. The tag must not be null.
	 *
	 * @param tag the tag to be added.
	 */
	public void addTag(Tag tag){
		ensureTags();
		tags.put(tag.getId(), tag);
		fireTagManagerEvent(new TagManagerEvent(this,TagManagerEvent.Type.ADDED,new Tag[]{tag}));
		saveTags();
	}

	/**
	 * Used to update the data for the tag.
	 *
	 * @param tag the tag being updated
	 */
	public void updateTag(Tag tag){
		ensureTags();
		if(tags.containsKey(tag.getId())){
			tags.put(tag.getId(), tag);
			fireTagManagerEvent(new TagManagerEvent(this,TagManagerEvent.Type.UPDATED,new Tag[]{tag}));
			saveTags();
		}
	}

	/**
	 * Used to delete the given tags.
	 *
	 * @param ts tags to be deleted
	 */
	public void deleteTags(Tag[] ts){
		if(tags == null || ts == null) return;
		
		for(Tag t : ts){
			tags.remove(t.getId());
		}
		
		if(ts.length != 0){
			fireTagManagerEvent(new TagManagerEvent(this,TagManagerEvent.Type.REMOVED,ts));
			saveTags();
		}
	}

	/**
	 * Used to retrieve an array of all tags in the tag set.
	 *
	 * @return the available tags
	 */
	public Tag[] getTags(){
		ensureTags();
		return(tags.values().toArray(new Tag[tags.size()]));
	}

	/**
	 * Used to add a TagManagerListener to the listener list. If the listener
	 * is already in the list, it will not be added again.
	 *
	 * @param listener the listener to be added
	 */
	public void addTagManagerListener(ITagManagerListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	/**
	 * Used to remove the specified listener from the listener list. If the
	 * listener is not in the list, no action will be taken.
	 *
	 * @param listener the listener to be removed.
	 */
	public void removeTagManagerListener(ITagManagerListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Helper used to extract tag names as a delimited string suitable for display purposes.
	 *
	 * @param tags the tags
	 * @param delim the delimiter
	 * @return a string of delimited tag names
	 */
	public static String extractTagNames(Tag[] tags, String delim){
		final StringBuilder str = new StringBuilder();
		for (Tag tag : tags){
			str.append(tag.getName()).append(delim);
		}
		if(tags.length > 0){
			str.delete(str.length()-delim.length(),str.length());
		}
		return(str.toString());
	}
	
	/**
	 * Used to extract an array of tag ids from the array of tags.
	 *
	 * @param tags the tags
	 * @return an array of tag ids
	 */
	public static String[] extractTagIds(Tag[] tags){
		final String[] uuids = new String[tags.length];
		for(int i=0; i<tags.length; i++){
			uuids[i] = tags[i].getId();
		}
		return(uuids);
	}

	/**
	 * Used to ensure that the tag map exists and has been loaded from its persisted state (if
	 * it has been persisted).
	 */
	private void ensureTags(){
		if(tags == null){
			tags = new HashMap<String, Tag>();
			loadTags();
		}
	}

	/**
	 * Load the tags from the persistance file.
	 */
	private void loadTags() {
		final File file = getTagFile();
		if(file.exists()){
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));

				final Tag[] _tags = TagIoFactory.create(TagIoFormat.MEMENTO).readTags(reader, null);
				for(Tag tag : _tags){
					tags.put(tag.getId(), tag);
				}

			} catch(Exception ex){
				TaggerLog.error(ex);
			} finally {
				IoUtils.closeQuietly(reader);
			}
		}
	}

	/**
	 * Save the tags to the persistance file.
	 */
	public void saveTags() {
		if(tags == null){return;}

		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(getTagFile()));
			TagIoFactory.create(TagIoFormat.MEMENTO).writeTags(writer, tags.values().toArray(new Tag[tags.size()]), null);
		} catch (Exception e) {
			TaggerLog.error(e);
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}

	/**
	 * Used to determine whether a tag exists in the system or not.
	 *
	 * @param tagId the tag id
	 * @return a value of true if the tag exists
	 */
	public boolean tagExists(String tagId) {
		ensureTags();
		return(tags.containsKey(tagId));
	}

	/**
	 * Used to retrieve the file in the plugin state directory used to store the tag set
	 * information.
	 *
	 * @return the tag set persistance file
	 */
	private File getTagFile() {
		return(TaggerActivator.getDefault().getStateLocation().append(TAGSETFILENAME).toFile());
	}

	/**
	 * Used to fire the given event to all TagManager listeners.
	 *
	 * @param tme the event to be fired.
	 */
	private void fireTagManagerEvent(TagManagerEvent tme){
		for (ITagManagerListener listener : listeners){
			listener.handleTagManagerEvent(tme);
		}
	}
}
