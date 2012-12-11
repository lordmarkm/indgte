package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.webapp.dto.BuyAndSellForm;

/**
 * Controller for Buy & Sell
 * @author mbmartinez
 */
@Controller
@RequestMapping("/t/")
public interface TradeController {
	
	/**
	 * Show 10 popular 10 recent
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView landing(Principal principal);

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView newItem(Principal principal, BuyAndSellForm form);
	
	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ModelAndView viewItem(Principal principal, long itemId);
	
	@RequestMapping(value = "/bid/{itemId}/{amount}.json", method = RequestMethod.POST)
	public JSON bid(Principal principal, long itemId, double amount);
	
	@RequestMapping(value = "/sold/{itemId}.json", method = RequestMethod.POST)
	public JSON sold(User user, long itemId);
	
	@RequestMapping(value = "/sidebar.json", method = RequestMethod.GET)
	public JSON getSidebarContent(UserExtension user);
	
	/* Tags */
	@RequestMapping(value = "/tags/{tag}")
	public ModelAndView tags(Principal principal, String tag);
	
	@RequestMapping(value = "/tags/{tag}/{start}/{howmany}.json")
	public JSON getTagItems(Principal principal, String tag, int start, int howmany);
	
	@RequestMapping(value = "/watchedtags.json", method = RequestMethod.GET)
	public JSON getAllWatchedTagItems(Principal principal);
	
	@RequestMapping(value = "/watchedtags/{tag}.json", method = RequestMethod.GET)
	public JSON getWatchedTagItems(Principal principal, String tag);
	
	@RequestMapping(value = "/watchedtags/{tag}.json", method = RequestMethod.POST)
	public JSON addToWatchedTags(Principal principal, String tag);
}
