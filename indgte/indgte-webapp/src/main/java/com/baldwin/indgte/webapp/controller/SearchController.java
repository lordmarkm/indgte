package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/s/")
public interface SearchController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView searchpage(Principal principal);
	
	@RequestMapping("/{term}")
	public ModelAndView search(Principal principal, String term);
	
	@RequestMapping("/{term}.json")
	public JSON autocomplete(Principal principal, String term);
	
	@RequestMapping("/own/{term}.json")
	public JSON autocompleteOwn(Principal principal, String term);
	
	@RequestMapping("/all/{term}.json")
	public JSON autocompleteAll(Principal principal, String term);
	
	@RequestMapping("/index/")
	public void reindex();
	
	@RequestMapping("/yellowpages/")
	public JSON getYellowPagesIndex();
	
	@RequestMapping(value = "/businesses/{categoryId}/{howmany}.json", method = RequestMethod.GET)
	public JSON getBusinessesForCategory(long categoryId, int howmany);
	
	@RequestMapping(value = "/categories/{groupId}", method = RequestMethod.GET)
	public ModelAndView viewCategory(Principal principal, long groupId);
	
	/**
	 * Returns a list of Object[String groupName, Number groupMembers]
	 */
	@RequestMapping(value = "/listablegroups.json")
	public JSON getListableGroups();
	
	/**
	 * Toptens search
	 */
	@RequestMapping(value = "/toptens/{term}/{start}/{howmany}.json", method = RequestMethod.GET)
	public JSON searchTopTens(Principal principal, String term, int start, int howmany);
	
	/**
	 * Tagcloud
	 */
	@RequestMapping(value = "/tags.json")
	public JSON getTags(Principal principal);
}