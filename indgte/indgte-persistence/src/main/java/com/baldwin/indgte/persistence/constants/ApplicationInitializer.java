package com.baldwin.indgte.persistence.constants;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.baldwin.indgte.pers‪istence.dao.CategoryDao;

/**
 * For "stuff" that needs initializing. Right now there's only cat dao
 * @author mbmartinez
 */

@Configuration
public class ApplicationInitializer {

	@Autowired 
	private CategoryDao catDao;
	
	/**
	 * Really only need to set this to true once per database
	 */
	@Value("${categories.reload}")
	private boolean reloadCategories;
	
	@PostConstruct
	public void init() {
		initCatDao();
	}
	
	private void initCatDao() {
		if(reloadCategories) catDao.init();
	}
}
