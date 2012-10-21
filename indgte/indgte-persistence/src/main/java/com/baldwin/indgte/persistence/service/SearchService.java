package com.baldwin.indgte.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class SearchService {
	
	static Logger log = LoggerFactory.getLogger(SearchService.class);
	
	@Autowired
	private BusinessDao businesses;
	
	public void reindex() {
		businesses.reindex();
	}

	public Map<SummaryType, List<Summary>> searchAll(String term, int maxResults) throws ParseException {
		List<Summary> businessResults = businesses.search(term, maxResults);
		List<Summary> productResults = businesses.searchProduct(term, maxResults);
		
		Map<SummaryType, List<Summary>> results = new HashMap<SummaryType, List<Summary>>();
		results.put(SummaryType.business, businessResults);
		results.put(SummaryType.product, productResults);
		
		log.debug("Search for completed, found {} businesses and {} products", businessResults.size(), productResults.size());
		return results;
	}

}
