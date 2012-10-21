package com.baldwin.indgte.pers‪istence.dao;

import com.baldwin.indgte.persistence.model.BusinessCategory;

public interface CategoryDao {
	public void init();
	public BusinessCategory get(long id);
	public BusinessCategory get(String name);
	public String getCategories(String firstLetter);
}
