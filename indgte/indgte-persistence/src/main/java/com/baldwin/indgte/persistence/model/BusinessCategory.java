package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "businessCategories")
public class BusinessCategory {
	@Id
	@GeneratedValue
	@Column(name = "categoryId")
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String description;

	public BusinessCategory() {
		//
	}
	
	public BusinessCategory(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name + ": " + description;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
