package com.baldwin.indgte.pers‪istence.dao;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;

public interface UserDao {
	/**
	 * Display name may differ between providers
	 */
	//User getByDisplayName(String displayName, String providerId);
	
	/**
	 * User id is common accross all providers
	 */
	User getByUsername(String username, String providerId);
	UserExtension getExtended(String targetUsername);
	UserExtension getExtended(String targetUsername, boolean createIfAbsent);
	UserExtension getExtended(long userId, Initializable... initialize);
	UserExtension getExtended(String username, Initializable... initialize);
	UserExtension getDefault(Initializable... initializables);
	String getImageUrl(String sender);
	void changeLocale(String name, String localeStr);

}