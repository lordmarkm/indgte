package com.baldwin.indgte.pers‪istence.dao;

import com.baldwin.indgte.persistence.model.User;

public interface UserDao {
	/**
	 * Display name may differ between providers
	 */
	//User getByDisplayName(String displayName, String providerId);
	
	/**
	 * User id is common accross all providers
	 */
	User getByUsername(String username, String providerId);
	User getBySSSUserId(String userId);
	User getFacebook(String userId);
}
