package com.baldwin.indgte.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao dao;
	
	public UserExtension getExtended(Long id, Initializable... initializables) {
		return dao.getExtended(id, initializables);
	}
	
	public UserExtension getExtended(String username, Initializable... initializables) {
		return dao.getExtended(username, initializables);
	}

	public void changeLocale(String name, String localeStr) {
		dao.changeLocale(name, localeStr);
	}

}