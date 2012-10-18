package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.dto.SearchResult;
import com.baldwin.indgte.persistence.service.SearchService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.SearchController;

@Component
public class SearchControllerImpl implements SearchController {
	
	static Logger log = LoggerFactory.getLogger(SearchControllerImpl.class);
	
	@Autowired
	private SearchService search;
	
	@Override
	public void reindex() {
		search.reindex();
	}

	@Override
	public ModelAndView searchpage() {
		return render("search/test").mav();
	}

	@Override
	public ModelAndView search(Principal principal, String term) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public @ResponseBody JSON autocomplete(Principal principal, @PathVariable String term) {
		long startTime = System.currentTimeMillis();
		try {
			JSON response = JSON.ok();
			for(Entry<SearchResult.ResultType, List<SearchResult>> result : search.searchAll(term, 5).entrySet()) {
				response.put(String.valueOf(result.getKey()), result.getValue());
			}
			response.put("searchtime", System.currentTimeMillis() - startTime);
			return response;
		} catch (Exception e) {
			log.error("Exception during search", e);
			return JSON.status500(e);
		}
	}
	
}
