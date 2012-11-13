package com.baldwin.indgte.persistence.service;

import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.dto.YellowPagesEntry;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.pers‪istence.dao.SearchDao;

@Service
public class SearchService {
	
	static Logger log = LoggerFactory.getLogger(SearchService.class);
	
	@Autowired
	private SearchDao dao;
	
	public void reindex() {
		dao.reindex();
	}

	public Map<SummaryType, List<Summary>> search(String term, int maxResults, SummaryType[] supportedTypes, String ownername) {
		Map<SummaryType, List<Summary>> results = new HashMap<SummaryType, List<Summary>>();
		
		for(SummaryType type : supportedTypes) {
			switch(type) {
			case business:
				results.put(business, dao.search(term, maxResults, BusinessProfile.class, ownername));
				break;
			case user:
				results.put(user, dao.search(term, maxResults, User.class, ownername));
				break;
			case category:
				results.put(category, dao.search(term, maxResults, Category.class, ownername));
				break;
			case product:
				results.put(product, dao.search(term, maxResults, Product.class, ownername));
				break;
			default:
				throw new IllegalArgumentException("Illegal type: " + type);
			}
		}
		
		if(log.isDebugEnabled()) {
			StringBuilder s = new StringBuilder("Search completed, found: ");
			for(Entry<SummaryType, List<Summary>> entry : results.entrySet()) {
				s.append(entry.getKey() + "-" + entry.getValue() == null ? "null" : entry.getValue().size() + ", ");
			}
			log.debug("Search completed, found: {}", s);
		}
		
		return results;
	}

	public MultiValueMap<String, Number> getYellowPagesIndex() {
		return dao.getListableGroups();
	}

	public List<Summary> getBusinesses(Long categoryId, int howmany) {
		return dao.getBusinesses(categoryId, howmany);
	}

	public List<YellowPagesEntry> getYellowPagesEntries(long categoryId) {
		return dao.getYellowPagesEntries(categoryId);
	}

	public BusinessGroup getBusinessGroup(long groupId) {
		return dao.getBusinessGroup(groupId);
	}

	@Deprecated
	public List<Object[]> getListableGroups() {
		return null;
	}

	public Collection<TopTenList> searchTopTenLists(String term, int start, int howmany) {
		return dao.searchTopTenLists(term, start, howmany);
	}
}