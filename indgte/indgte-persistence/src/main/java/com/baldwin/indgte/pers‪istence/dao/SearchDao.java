package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.util.MultiValueMap;

import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.YellowPagesEntry;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.TopTenList;

public interface SearchDao {
	void reindex();

	/**
	 * IMPORTANT: owner name must be null if c == User.class, else explode! (Actually, OwnerSummarizer
	 * will throw an IllegalArgumentException because slavery has always been illegal in the Philippines)
	 */
	List<Summary> search(String term, int maxResults, Class<? extends Summarizable> c, String ownername);
	
	/**
	 * For the yellow pages index
	 */
	MultiValueMap<String, Number> countBusinesses();
	
	void test();

	List<Summary> getBusinesses(Long categoryId, int howmany);

	List<YellowPagesEntry> getYellowPagesEntries(long categoryId);

	BusinessGroup getBusinessGroup(long groupId);

	MultiValueMap<String, Number> getListableGroups();

	Collection<TopTenList> searchTopTenLists(String term, int start, int howmany);
}