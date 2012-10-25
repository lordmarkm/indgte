package com.baldwin.indgte.persistence.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Summarizer implements ResultTransformer {
	private static final long serialVersionUID = 7702355874980996266L;

	static Logger log = LoggerFactory.getLogger(Summarizer.class);
	
	/**
	 * This method has never been tested and will probably exploed
	 */
	@Override
	public Object transformTuple(Object[] tuples, String[] aliases) {
		throw new IllegalStateException("Not used");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List transformList(List collection) {
		log.debug("Transforming {} items", collection.size());
		
		List transformed = new ArrayList();
		for(Summarizable searchable : (List<Summarizable>)collection) {
			transformed.add(searchable.summarize());
		}
		
		return transformed;
	}

}
