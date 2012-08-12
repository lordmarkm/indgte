package com.baldwin.indgte.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="INDGTE_PROFILE_BUSINESS")
public class BusinessProfile {
	@Id @GeneratedValue @Column(name="BUSINESS_ID")
	private long id;
	
	@Column(name="DOMAIN", nullable=false, unique=true)
	private String domain;
	
	@Column(name="LONG_NAME")
	private String fullName;
	
	@Column
	private String street;
	
	@Column
	private String city;
	
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable(name="USER_BUSINESS_MAPPING", 
		joinColumns={@JoinColumn(name="BUSINESS_ID")},
		inverseJoinColumns={@JoinColumn(name="USER_ID")})
	private List<User> admins;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Page> pages;

	@Override
	public String toString() {
		return domain + ":" + fullName;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<User> getAdmins() {
		return admins;
	}

	public void setAdmins(List<User> admins) {
		this.admins = admins;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
}
