package com.baldwin.indgte.persistence.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.dto.SearchResult;
import com.baldwin.indgte.persistence.dto.SearchResult.ResultType;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class SearchService {
	
	@Autowired
	private BusinessDao businesses;
	
	public void reindex() {
		businesses.reindex();
	}

	public Map<ResultType, List<SearchResult>> searchAll(String term) throws ParseException {
		List<SearchResult> businessResults = businesses.search(term);
		Map<ResultType, List<SearchResult>> results = new HashMap<ResultType, List<SearchResult>>();
		
		results.put(ResultType.business, businessResults);
		return results;
	}

}
