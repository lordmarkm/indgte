package com.baldwin.indgte.webapp.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Controller
@RequestMapping("/o/")
public interface PromoteController {
	
	@RequestMapping(value = "/promotepost/{postId}", method = RequestMethod.POST)
	public String promotePost(Principal principal, long postId, Date startDate, Date endDate);

	@RequestMapping(value = "/sidebar/{type}/{id}")
	public String promoteSidebar(Principal principal, SummaryType type, long id, Date start, Date end);
}
