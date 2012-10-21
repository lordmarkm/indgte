package com.baldwin.indgte.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.pers‪istence.dao.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao dao;
	
	public User getByUsername(String userId, String providerId) {
		return dao.getByUsername(userId, providerId);
	}

	public User getMain(String userId) {
		return dao.getSpring(userId);
	}

	public User getFacebook(String userId) {
		return dao.getFacebook(userId);
	}
}