package com.baldwin.indgte.pers‪istence.dao;

import java.util.List;

import com.baldwin.indgte.persistence.dto.Summary;

public interface SearchDao {
	void reindex();
	List<Summary> search(String term, int maxResults);
	List<Summary> searchProduct(String term, int maxResults);
	List<Summary> searchCategory(String term, int maxResults);
	List<Summary> search(String term, int maxResults, String ownername);
	List<Summary> searchCategory(String term, int maxResults, String ownername);
	List<Summary> searchProduct(String term, int maxResults, String ownername);
}