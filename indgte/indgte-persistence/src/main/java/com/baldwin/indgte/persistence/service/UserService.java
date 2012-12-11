package com.baldwin.indgte.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao dao;
	
	@Deprecated
	public User getByUsername(String userId, String providerId) {
		return dao.getByUsername(userId, providerId);
	}

//	@Deprecated
//	public User getMain(String userId) {
//		return dao.getSpring(userId);
//	}
//
//	@Deprecated
//	public User getFacebook(String userId) {
//		return dao.getFacebook(userId);
//	}

	public UserExtension getExtended(String username, Initializable... initializables) {
		return dao.getExtended(username, initializables);
	}
}