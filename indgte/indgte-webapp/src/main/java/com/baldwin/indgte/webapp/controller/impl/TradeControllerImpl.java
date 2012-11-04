package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

import java.security.Principal;
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

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.TradeService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.TradeController;
import com.baldwin.indgte.webapp.dto.BuyAndSellForm;

@Component
@SessionAttributes(value={"user"}, types={User.class})
public class TradeControllerImpl implements TradeController {

	static Logger log = LoggerFactory.getLogger(TradeControllerImpl.class);
	
	@Autowired
	private UserService users;
	
	@Autowired TradeService trade;
	
	@Override
	public ModelAndView landing(Principal principal) {
		User user = users.getFacebook(principal.getName());
		
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
	public ModelAndView newItem(User user, BuyAndSellForm form) {
		BuyAndSellItem item = form.getItem();
		if(null != item) {
			trade.save(user, item);
			return redirect("/t/" + item.getId()).mav();
		}
		return render(user, "error").mav();
	}
	
	@Override
	public @ResponseBody JSON getSidebarContent(@ModelAttribute User user) {
		return JSON.ok().put("user", user);
	}
	
	@Override
	public ModelAndView viewItem(Principal principal, @PathVariable long itemId) {
		log.debug("{} viewing item with id {}", principal.getName(), itemId);
		User user = users.getFacebook(principal.getName());
		BuyAndSellItem item = trade.get(principal.getName(), itemId);
		
		return render(user, "buyandsellitem")
					.put("item", item)
					.put("owner", item.getOwner().getUsername().equals(principal.getName()))
					.mav();
	}


	@Override
	public JSON sold(User user, long itemId) {
		// TODO Auto-generated method stub
		return null;
	}


}
