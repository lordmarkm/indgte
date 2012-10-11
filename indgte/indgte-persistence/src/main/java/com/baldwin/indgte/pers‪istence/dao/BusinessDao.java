package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.Imgur;

public interface BusinessDao {
	BusinessProfile get(String bizName);
	void create(BusinessProfile bizProfile, String owner);
	void update(BusinessProfile bizProfile);
	void delete(BusinessProfile bizProfile);
	Collection<BusinessProfile> getBusinesses(String userId);
	void saveProfilepic(String domain, Imgur profilepic);
	void saveCoverpic(String domain, Imgur coverpic);
}