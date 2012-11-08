package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "businessCategories")
public class BusinessGroup {
	@Id
	@GeneratedValue
	@Column(name = "categoryId")
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String description;

	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="businessGroup")
	private Set<BusinessProfile> businesses;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="topTenListId")
	private TopTenList topTenList;
	
	public BusinessGroup() {
		//
	}
	
	public BusinessGroup(String name) {
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

	@JsonIgnore
	public Set<BusinessProfile> getBusinesses() {
		if(null == businesses) {
			businesses = new HashSet<BusinessProfile>();
		}
		return businesses;
	}

	public void setBusinesses(Set<BusinessProfile> businesses) {
		this.businesses = businesses;
	}

	public TopTenList getTopTenList() {
		return topTenList;
	}

	public void setTopTenList(TopTenList topTenList) {
		this.topTenList = topTenList;
	}
}
