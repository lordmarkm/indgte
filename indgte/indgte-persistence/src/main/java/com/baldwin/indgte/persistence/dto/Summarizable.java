package com.baldwin.indgte.persistence.dto;

public interface Summarizable {
	Summary summarize();
	String[] getSearchableFields();
}
