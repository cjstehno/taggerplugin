package net.sourceforge.taggerplugin.model;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

public class TagTest {
	
	private static final String[] IDS = {UUID.randomUUID().toString(),UUID.randomUUID().toString()};
	private static final String[] NAMES = {"TagName","OtherName"};
	private static final String[] DESCRIPTIONS = {"A Tag Description","Some Other Description"};
	private static final Tag[] TAGS = {new Tag(IDS[0],NAMES[0],DESCRIPTIONS[0]), new Tag(IDS[1],NAMES[1],DESCRIPTIONS[1])};

	@Test
	public void settersAndGetters(){
		Assert.assertEquals(IDS[0], TAGS[0].getId());
		Assert.assertEquals(NAMES[0], TAGS[0].getName());
		Assert.assertEquals(DESCRIPTIONS[0], TAGS[0].getDescription());
		
		TAGS[0].setId(IDS[1]);
		TAGS[0].setName(NAMES[1]);
		TAGS[0].setDescription(DESCRIPTIONS[1]);
		
		Assert.assertEquals(IDS[1], TAGS[0].getId());
		Assert.assertEquals(NAMES[1], TAGS[0].getName());
		Assert.assertEquals(DESCRIPTIONS[1], TAGS[0].getDescription());
	}
	
	@Test
	public void selfEquality(){
		Assert.assertSame(TAGS[0],TAGS[0]);
		Assert.assertEquals(TAGS[0], TAGS[0]);
	}
	
	@Test 
	public void notEqualDifferent(){/// settersAndGetters is changing the value!!!
		Assert.assertNotSame(TAGS[0], TAGS[1]);
		Assert.assertFalse(TAGS[0].equals(TAGS[1]));
	}
	
	@Test
	public void equalToSameData(){
		final Tag sameTag = new Tag(TAGS[0].getId(),TAGS[0].getName(),TAGS[0].getDescription());
		Assert.assertNotSame(TAGS[0],sameTag);
		Assert.assertEquals(TAGS[0], sameTag);
	}
	
//	@Test
//	public void comparable(){
//		final List<Tag> tags = new ArrayList<Tag>();
//		tags.add();
//		tags.add(o);
//		tags.add(o);
//	}
	
	/*
	 * comparable
	 * hash
	 */
}
