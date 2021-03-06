package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.business;
import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.buyandsellitem;
import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.category;
import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.product;
import static com.baldwin.indgte.persistence.dto.Summary.SummaryType.user;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.dto.YellowPagesEntry;
import com.baldwin.indgte.persistence.model.BusinessGroup;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.SearchService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.controller.SearchController;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;
import com.baldwin.indgte.webapp.misc.DgteConstants;

@Component
public class SearchControllerImpl implements SearchController {
	
	static Logger log = LoggerFactory.getLogger(SearchControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired
	private SearchService search;

	@Autowired
	private ConstantsInserterBean constants;
	
	@Override
	public void reindex() {
		search.reindex();
	}

	@Override
	public ModelAndView yellowpages(Principal principal) {
		log.info("Yellowpages requested. Principal: {}", principal == null ? "Anonymous" : principal);
		
		MultiValueMap<String, Number> count = search.getYellowPagesIndex();
		log.debug("Business count: {}", count);
		
		MavBuilder mav = render("yellowpages")
				.put("businesses", count);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user);
		}
		
		return mav.mav();
	}
	
	/*
	 * Search
	 */

	@Override
	public ModelAndView search(Principal principal, @PathVariable String term) throws Exception {
		long start = System.currentTimeMillis();

		log.info("Search for term {} from {}", term, principal == null ? "Anonymous" : principal.getName());
		
		MavBuilder mav = render("fullsearch")
				.put("term", term)
				.put("maxresults", DgteConstants.FULLSEARCH_MAXRESULTS);
		int total = 0;
		
		Map<SummaryType, List<Summary>> results = search.search(term, 0, DgteConstants.FULLSEARCH_MAXRESULTS, SummaryType.values(), null);
		for(Entry<Summary.SummaryType, List<Summary>> result : results.entrySet()) {
			SummaryType key = result.getKey();
			List<Summary> entities = result.getValue();
			if(null != entities && entities.size() > 0) {
				mav.put(key.name().toUpperCase(), entities);
			} else {
				log.debug("Ignoring [{}]", key.name());
			}
			total += entities.size();
		}
		
		mav.put("total", total);

		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user);
		}
		
		log.debug("Search took {} ms", System.currentTimeMillis() - start);
		
		constants.insertConstants(mav);
		return mav.mav();
	}
	
	@Override
	public @ResponseBody JSON autocomplete(Principal principal, @PathVariable String term) {
		long startTime = System.currentTimeMillis();
		int maxresults = -1;
		SummaryType[] supportedTypes = new SummaryType[] {business, category, product, buyandsellitem};

		JSON results = search(principal, term, supportedTypes, maxresults, null);
		return results.put("searchtime", System.currentTimeMillis() - startTime);
	}

	@Override
	public @ResponseBody JSON autocompleteOwn(Principal principal, @PathVariable String term) {
		long startTime = System.currentTimeMillis();
		int maxresults = -1;
		SummaryType[] supportedTypes = new SummaryType[] {business, category, product, buyandsellitem};
		
		JSON results = search(principal, term, supportedTypes, maxresults, principal.getName());
		return results.put("searchtime", System.currentTimeMillis() - startTime);
	}

	@Override
	public @ResponseBody JSON autocompleteAll(Principal principal, @PathVariable String term) {
		long startTime = System.currentTimeMillis();
		int maxresults = -1;
		SummaryType[] supportedTypes = new SummaryType[] {user, business, category, product};
		
		JSON results = search(principal, term, supportedTypes, maxresults, null);
		return results.put("searchtime", System.currentTimeMillis() - startTime);
	}
	
	private JSON search(Principal principal, String term, SummaryType[] supportedTypes, int maxresults, String ownername) {
		try {
			JSON response = JSON.ok();
			for(Entry<Summary.SummaryType, List<Summary>> result : search.search(term, 0, maxresults, supportedTypes, ownername).entrySet()) {
				response.put(String.valueOf(result.getKey()), result.getValue());
			}
			return response;
		} catch (Exception e) {
			log.error("Exception during search", e);
			return JSON.status500(e);
		}
	}
	
	/*
	 * Yellow Pages
	 */
	@Override
	public @ResponseBody JSON getYellowPagesIndex() {
		try {
			return JSON.ok().put("businesses", search.getYellowPagesIndex());
		} catch (Exception e) {
			log.error("Error getting Yellow Pages index.", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getBusinessesForCategory(@PathVariable long categoryId, @PathVariable int howmany) {
		try {
			return JSON.ok().put("businesses", search.getBusinesses(categoryId, howmany));
		} catch (Exception e) {
			log.error("Error getting businesses for category id " + categoryId, e);
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView viewCategory(Principal principal, @PathVariable long groupId) {
		BusinessGroup group = search.getBusinessGroup(groupId);
		List<YellowPagesEntry> businesses = search.getYellowPagesEntries(groupId);

		log.info("Business group page for group {} with {} businesses requested by {}", group.getName(), businesses.size(), principal == null ? "Anonymous" : principal.getName());
		
		MavBuilder mav = render("yellowpage")
				.put("group", group)
				.put("businesses", businesses);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user);
		}
		
		return mav.mav();
	}

	@Override
	public JSON getListableGroups() {
		return JSON.ok();
	}

	@Override
	public @ResponseBody JSON searchTopTens(Principal principal, @PathVariable String term, @PathVariable int start, @PathVariable int howmany) {
		try {
			return JSON.ok().put("lists", search.searchTopTenLists(term, start, howmany));
		} catch (Exception e) {
			log.error("Exception while searching toptens", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getTags(Principal principal) {
		try {
			Collection<Tag> tags = search.getTags(Tag.SortColumn.numberofitems, 20);
			Collections.sort((List<Tag>) tags);
			return JSON.ok().put("tags", tags);
		} catch (Exception e) {
			log.error("Error getting weighted tags", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON searchBuySell(Principal principal, @PathVariable String term, @PathVariable int start, @PathVariable int howmany) {
		try {
			return JSON.ok().put("items", search.searchBuySell(term, start, howmany));
		} catch (Exception e) {
			log.error("Exception searching buy and sell items", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON searchTag(Principal principal, @PathVariable String tag, @PathVariable String term, @PathVariable int start, @PathVariable int howmany) {
		try {
			return JSON.ok().put("items", search.searchBuySellTag(tag, term, start, howmany));
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException() {
		return "redirect:/error/";
	}
}
