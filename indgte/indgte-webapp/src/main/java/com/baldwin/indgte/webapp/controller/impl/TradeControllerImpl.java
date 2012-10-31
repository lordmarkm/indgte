package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.*;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BuyAndSellService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.TradeController;

@Component
@SessionAttributes(value={"user"}, types={User.class})
public class TradeControllerImpl implements TradeController {

	@Autowired
	private UserService users;
	
	@Autowired BuyAndSellService bas;
	
	@Override
	public ModelAndView landing(Principal principal) {
		User user = users.getFacebook(principal.getName());
		
		Collection<BuyAndSellItem> popular = bas.getPopular(0, 10);
		Collection<BuyAndSellItem> recent = bas.getRecent(0, 10);
		Collection<BuyAndSellItem> owned = bas.getOwned(user);
		
		return render(user, "buyandsell")
				.put("popular", popular)
				.put("recent", recent)
				.put("ownded", owned)
				.mav();
	}

	@Override
	public @ResponseBody JSON getSidebarContent(@ModelAttribute User user) {
		return JSON.ok().put("user", user);
	}

	@Override
	public ModelAndView viewItem(Principal principal, long itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSON newItem(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSON sold(User user, long itemId) {
		// TODO Auto-generated method stub
		return null;
	}
}
