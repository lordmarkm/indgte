package com.baldwin.indgte.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.pers‪istence.dao.SearchDao;

@Service
public class SearchService {
	
	static Logger log = LoggerFactory.getLogger(SearchService.class);
	
	@Autowired
	private SearchDao dao;
	
	public void reindex() {
		dao.reindex();
	}

	public Map<SummaryType, List<Summary>> searchAll(String term, int maxResults) {
		List<Summary> businessResults = dao.search(term, maxResults);
		List<Summary> categoryResults = dao.searchCategory(term, maxResults);
		List<Summary> productResults = dao.searchProduct(term, maxResults);
		
		Map<SummaryType, List<Summary>> results = new HashMap<SummaryType, List<Summary>>();
		results.put(SummaryType.business, businessResults);
		results.put(SummaryType.category, categoryResults);
		results.put(SummaryType.product, productResults);
		
		log.debug("Search for completed, found [businesses, categories, products] {}", new int[]{businessResults.size(), categoryResults.size(), productResults.size()});
		return results;
	}

	public Map<SummaryType, List<Summary>> searchOwn(String term, int maxResults, String ownername) {
		List<Summary> businessResults = dao.search(term, maxResults, ownername);
		List<Summary> categoryResults = dao.searchCategory(term, maxResults, ownername);
		List<Summary> productResults = dao.searchProduct(term, maxResults, ownername);
		
		Map<SummaryType, List<Summary>> results = new HashMap<SummaryType, List<Summary>>();
		results.put(SummaryType.business, businessResults);
		results.put(SummaryType.category, categoryResults);
		results.put(SummaryType.product, productResults);
		
		log.debug("Search own businesses completed, found [businesses, categories, products] {}", new int[]{businessResults.size(), categoryResults.size(), productResults.size()});
		return results;
	}

}
