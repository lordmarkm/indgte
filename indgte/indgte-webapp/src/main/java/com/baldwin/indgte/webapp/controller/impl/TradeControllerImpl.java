package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.NewBidNotification;
import com.baldwin.indgte.persistence.model.Tag;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.NotificationsService;
import com.baldwin.indgte.persistence.service.TradeService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.controller.TradeController;
import com.baldwin.indgte.webapp.dto.BuyAndSellForm;
import com.baldwin.indgte.webapp.misc.Comet;

@SuppressWarnings("unused")
@Component
@SessionAttributes(value={"user"})
public class TradeControllerImpl implements TradeController {

	static Logger log = LoggerFactory.getLogger(TradeControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired 
	private TradeService trade;
	
	@Autowired
	private Comet comet;
	
	@Autowired
	private NotificationsService notifs;
	
	@Override
	public ModelAndView landing(Principal principal) {
		log.info("Trade page requested by {}", principal == null ? "Anonymous" : principal.getName());
		
		Collection<BuyAndSellItem> popular = trade.getPopular(0, 9);
		Collection<BuyAndSellItem> recent = trade.getRecent(0, 9);
		
		MavBuilder mav = render("buyandsell")
				.put("popular", popular)
				.put("recent", recent);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName(), Initializable.watchedtags, Initializable.buyandsellitems);
			Collection<BuyAndSellItem> owned = trade.getOwned(user);
			
			mav.put("user", user)
			   .put("owned", owned);
		}
		
		return mav.mav();
	}

	@Override
	public ModelAndView newItem(Principal principal, BuyAndSellForm form) {
		BuyAndSellItem item = form.getItem();
		
		log.info("New Buy and Sell item {} created by {}", item.getName(), principal.getName());
		
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
		
		BuyAndSellItem item = trade.get(null == principal ? null : principal.getName(), itemId);
		
		log.info("View request for buy and sell item {} from {}", item.getName(), null == principal ? "Anonymous" : principal.getName());
		
		if(null != item) {
			MavBuilder builder = render("buyandsellitem")
					.put("item", item);
			
			if(null != item.getDescription()) builder.description(item.getDescription());
			if(null != item.getImgur()) builder.thumbnail(item.getImgur().getSmallSquare());
			
			if(item instanceof AuctionItem) {
				AuctionItem auctionItem = (AuctionItem)item;
				builder.put("bidIncrement", trade.getBidIncrement());
				builder.put("finished", System.currentTimeMillis() - auctionItem.getBiddingEnds().getTime() > 0);
			}
			
			if(null != principal) {
				UserExtension user = users.getExtended(principal.getName(), Initializable.wishlist, Initializable.watchedtags);
				builder.put("user", user)
					.put("inwishlist", user.inWishlist(item))
					.put("owner", item.getOwner().equals(user));
			}
			
			return builder.mav();
		}
		
		else {
			return render("buyandsellnotfound").mav();
		}
	}
	
	@Override
	public @ResponseBody JSON bid(Principal principal, @PathVariable long itemId, @PathVariable double amount) {

		log.info("New bid on item with id {} ({}) from {}", itemId, amount, principal.getName());
		
		try {
			double minimum = trade.bid(principal.getName(), itemId, amount);
			
			if(minimum == -1) { //auction has finished
				return JSON.status404().message("auction is finished");
			} else if(minimum == 0) { //last bid is winning
				Collection<NewBidNotification> newnotifs = notifs.newBid(itemId);
				for(NewBidNotification notif : newnotifs) {
					comet.fireNotif(notif);
				}
				return JSON.ok();
			} else {
				return JSON.teapot().put("minimum", minimum);
			}
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON sold(Principal principal, @PathVariable long itemId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean moderator = isModerator(auth);
		
		try {
			trade.sold(moderator, principal.getName(), itemId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON available(Principal principal, @PathVariable long itemId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean moderator = isModerator(auth);
		
		try {
			trade.available(moderator, principal.getName(), itemId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON delete(Principal principal, @PathVariable long itemId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean moderator = isModerator(auth);
			
			trade.delete(moderator, principal.getName(), itemId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView tags(Principal principal, @PathVariable String tag) {
		Tag actualTag = trade.getTag(tag);
		Collection<BuyAndSellItem> items; 
		if(null != actualTag) { // don't bother loading if tag is not found
			items = trade.getItemsWithTag(tag, 0, 10);
		} else {
			items = new ArrayList<BuyAndSellItem>();
		}
		
		MavBuilder mav = render("buyandselltag")
					.put("items", items)
					.put("tag", actualTag)
					.put("tagString", tag);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName(), Initializable.watchedtags);

			mav.put("user", user)
			   .put("watched", user.getWatchedTags().contains(actualTag));
		}
					
		return mav.mav();
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
	
	private boolean isModerator(Authentication auth) {
		log.debug("Checking authentication. Authorities: {}", auth.getAuthorities());
		for(GrantedAuthority g : auth.getAuthorities()) {
			if(g.getAuthority().equals("ROLE_MODERATOR")) return true;
		}
		return false;
	}
	
	@ExceptionHandler(Exception.class)
	public String error(Exception e) {
		log.error("Exception!", e);
		return "redirect:/error/";
	}
}
