package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.TradeService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.controller.TradeController;
import com.baldwin.indgte.webapp.dto.BuyAndSellForm;

@Component
@SessionAttributes(value={"user"})
public class TradeControllerImpl implements TradeController {

	static Logger log = LoggerFactory.getLogger(TradeControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired TradeService trade;
	
	@Override
	public ModelAndView landing(Principal principal) {
		UserExtension user = users.getExtended(principal.getName(), Initializable.watchedtags, Initializable.buyandsellitems);
		
		Collection<BuyAndSellItem> popular = trade.getPopular(0, 9);
		Collection<BuyAndSellItem> recent = trade.getRecent(0, 9);
		Collection<BuyAndSellItem> owned = trade.getOwned(user);
		
		return render(user, "buyandsell")
				.put("popular", popular)
				.put("recent", recent)
				.put("owned", owned)
				.mav();
	}

	@Override
	public ModelAndView newItem(Principal principal, BuyAndSellForm form) {
		BuyAndSellItem item = form.getItem();
		UserExtension user = users.getExtended(principal.getName());
		if(null != item) {
			trade.save(principal.getName(), item);
			return redirect("/t/" + item.getId()).mav();
		}
		return render(user, "error").mav();
	}
	
	@Override
	public @ResponseBody JSON getSidebarContent(@ModelAttribute UserExtension user) {
		return JSON.ok().put("user", user);
	}
	
	@Override
	public ModelAndView viewItem(Principal principal, @PathVariable long itemId) {
		log.debug("{} viewing item with id {}", principal.getName(), itemId);
		UserExtension user = users.getExtended(principal.getName(), Initializable.wishlist);
		BuyAndSellItem item = trade.get(principal.getName(), itemId);
		
		MavBuilder builder = render(user, "buyandsellitem")
					.put("item", item)
					.put("inwishlist", user.inWishlist(item))
					.put("owner", item.getOwner().equals(user.getUser()));
		
		if(item instanceof AuctionItem) {
			AuctionItem auctionItem = (AuctionItem)item;
			builder.put("bidIncrement", trade.getBidIncrement());
			builder.put("finished", System.currentTimeMillis() - auctionItem.getBiddingEnds().getTime() > 0);
		}
		
		return builder.mav();
	}
	
	@Override
	public @ResponseBody JSON bid(@ModelAttribute User user, @PathVariable long itemId, @PathVariable double amount) {
		try {
			double minimum = trade.bid(user, itemId, amount);
			
			if(minimum == -1) { //auction has finished
				return JSON.status404().message("auction is finished");
			} else if(minimum == 0) { //last bid is winning
				return JSON.ok();
			} else {
				return JSON.teapot().put("minimum", minimum);
			}
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON sold(User user, long itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView tags(Principal principal, @PathVariable String tag) {
		UserExtension user = users.getExtended(principal.getName(), Initializable.watchedtags);
		Tag actualTag = trade.getTag(tag);
		Collection<BuyAndSellItem> items; 
		if(null != actualTag) { // don't bother loading if tag is not found
			items = trade.getItemsWithTag(tag, 0, 10);
		} else {
			items = new ArrayList<BuyAndSellItem>();
		}
		
		return render(user, "buyandselltag")
					.put("items", items)
					.put("tag", actualTag)
					.put("tagString", tag)
					.put("watched", user.getWatchedTags().contains(actualTag))
					.mav();
	}

	@Override
	public @ResponseBody JSON getTagItems(Principal principal, @PathVariable String tag, @PathVariable int start, @PathVariable int howmany) {
		try {
			return JSON.ok().put("items", trade.getItemsWithTag(tag, start, howmany));
		} catch (Exception e) {
			log.error("Error loading more items for tag.", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getAllWatchedTagItems(Principal principal) {
		try {
			return JSON.ok().put("items", trade.getWatchedTagItems(principal.getName(), 0, 5));
		} catch (Exception e) {
			log.error("Error getting watched tag items.", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON getWatchedTagItems(Principal principal, @PathVariable String tag) {
		try {
			return JSON.ok().put("items", trade.getWatchedTagItems(principal.getName(), tag, 0, 5));
		} catch (Exception e) {
			log.error("Error getting watched tag items.", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON addToWatchedTags(Principal principal, @PathVariable String tag) {
		try {
			trade.addToWatchedTags(principal.getName(), tag);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error adding watched tag", e);
			return JSON.status500(e);
		}
	}
}
