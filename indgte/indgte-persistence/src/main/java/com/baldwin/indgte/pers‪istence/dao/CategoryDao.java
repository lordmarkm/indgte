package com.baldwin.indgte.pers‪istence.dao;

import com.baldwin.indgte.persistence.model.BusinessGroup;

public interface CategoryDao {
	public void init();
	public BusinessGroup get(long id);
	public BusinessGroup get(String name);
	public String getCategories(String firstLetter);
}
