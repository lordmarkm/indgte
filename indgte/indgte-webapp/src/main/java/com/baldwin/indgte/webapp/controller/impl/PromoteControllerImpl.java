package com.baldwin.indgte.webapp.controller.impl;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.service.BillingService;
import com.baldwin.indgte.webapp.controller.PromoteController;

@Component
public class PromoteControllerImpl implements PromoteController {

	@Autowired
	private BillingService billing;
	
	@Override
	public String promotePost(Principal principal, @PathVariable long postId, 
			@RequestParam @DateTimeFormat(pattern="MM/dd/yyyy") Date startDate, 
			@RequestParam @DateTimeFormat(pattern="MM/dd/yyyy") Date endDate) {

		billing.promotePost(principal.getName(), postId, startDate, endDate);
		return "redirect:/i/posts/" + postId;
	}

	@Override
	public String promoteSidebar(Principal principal, @PathVariable SummaryType type, @PathVariable long id, 
			@RequestParam @DateTimeFormat(pattern="MM/dd/yyyy") Date start, 
			@RequestParam @DateTimeFormat(pattern="MM/dd/yyyy") Date end) {

		billing.promoteSidebar(principal.getName(), type, id, start, end);
		return "redirect:/";
	}

}
