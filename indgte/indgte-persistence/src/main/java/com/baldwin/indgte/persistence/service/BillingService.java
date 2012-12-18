package com.baldwin.indgte.persistence.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.BillingDao;

@Service
public class BillingService {

	@Autowired
	private BillingDao bills;
	
	public UserExtension grantCoconuts(String adminName, long receiverId, int howmany) {
		return bills.grantCoconuts(adminName, receiverId, howmany);
	}

	public void promotePost(String username, long postId, Date startDate, Date endDate) {
		long diffTime = endDate.getTime() - startDate.getTime();
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		int coconutCost = (int) diffDays + 1;
		
		bills.promotePost(username, startDate, endDate, coconutCost, postId);
	}

	public void promoteSidebar(String username, SummaryType type, long id, Date start, Date end) {
		
		long diffTime = end.getTime() - start.getTime();
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		int coconutCost = (int) diffDays + 1;

		bills.promoteSidebar(username, type, id, start, end, coconutCost);
	}
	
}
