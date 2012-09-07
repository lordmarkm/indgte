package com.baldwin.indgte.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;

@Service
public class BusinessService {
	@Autowired BusinessDao dao;
	
	public BusinessProfile get(String name) {
		return dao.get(name);
	}
	public void save(BusinessProfile bizProfile) {
		dao.save(bizProfile);
	}
	public void delete(BusinessProfile bizProfile) {
		dao.delete(bizProfile);
	}
}
