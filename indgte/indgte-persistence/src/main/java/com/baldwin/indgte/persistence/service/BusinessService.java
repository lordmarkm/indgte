package com.baldwin.indgte.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class BusinessService {
	@Autowired BusinessDao bizDao;
	
	public BusinessProfile get(String bizName) {
		return bizDao.get(bizName);
	}
	public void save(BusinessProfile bizProfile) {
		bizDao.save(bizProfile);
	}
	public void delete(BusinessProfile bizProfile) {
		bizDao.delete(bizProfile);
	}
}
