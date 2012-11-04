package com.baldwin.indgte.webapp.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.User;
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
	public ModelAndView newItem(User user, BuyAndSellForm form);
	
	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ModelAndView viewItem(Principal principal, long itemId);
	
	@RequestMapping(value = "/sold/{itemId}.json", method = RequestMethod.POST)
	public JSON sold(User user, long itemId);
	
	@RequestMapping(value = "/sidebar.json", method = RequestMethod.GET)
	public JSON getSidebarContent(User user);
}
