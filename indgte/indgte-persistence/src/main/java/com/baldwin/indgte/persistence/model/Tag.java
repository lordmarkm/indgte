package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tags")
public class Tag {
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private String tag;

	@Column
	private Long items;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getItems() {
		return items;
	}

	public void setItems(Long items) {
		this.items = items;
	}
}
