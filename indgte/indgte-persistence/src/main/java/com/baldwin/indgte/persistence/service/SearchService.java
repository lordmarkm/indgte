package com.baldwin.indgte.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.dto.SearchResult;
import com.baldwin.indgte.persistence.dto.SearchResult.ResultType;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class SearchService {
	
	static Logger log = LoggerFactory.getLogger(SearchService.class);
	
	@Autowired
	private BusinessDao businesses;
	
	public void reindex() {
		businesses.reindex();
	}

	public Map<ResultType, List<SearchResult>> searchAll(String term, int maxResults) throws ParseException {
		List<SearchResult> businessResults = businesses.search(term, maxResults);
		List<SearchResult> productResults = businesses.searchProduct(term, maxResults);
		
		Map<ResultType, List<SearchResult>> results = new HashMap<ResultType, List<SearchResult>>();
		results.put(ResultType.business, businessResults);
		results.put(ResultType.product, productResults);
		
		log.debug("Search for completed, found {} businesses and {} products", businessResults.size(), productResults.size());
		return results;
	}

}
