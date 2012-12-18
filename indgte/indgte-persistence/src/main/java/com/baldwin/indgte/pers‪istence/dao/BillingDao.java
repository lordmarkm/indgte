package com.baldwin.indgte.pers‪istence.dao;

import java.util.Date;

import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.UserExtension;

public interface BillingDao {

	UserExtension grantCoconuts(String adminName, long receiverId, int howmany);

	void promotePost(String username, Date startDate, Date endDate,	int coconutCost, long postId);

	void promoteSidebar(String username, SummaryType type, long id, Date start, Date end, int coconutCost);

}
