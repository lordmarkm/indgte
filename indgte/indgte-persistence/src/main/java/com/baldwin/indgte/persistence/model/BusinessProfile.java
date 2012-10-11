package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="businesses")
public class BusinessProfile {
	@Id @GeneratedValue @Column(name="business_id")
	private long id;
	
	@Column(nullable=false, unique=true)
	private String domain;
	
	@Column(nullable=false)
	private String fullName;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="profilepicId")
	private Imgur profilepic;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="coverpicId")
	private Imgur coverpic;
	
	@Column
	private String description;
	
	@Column
	private String address;
	
	@Column 
	private String email;
	
	@Column
	private String landline;
	
	@Column
	private String cellphone;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(
		name="owner_id", 
		referencedColumnName="connection_id", 
		nullable=false
	)
	private User owner;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="business")
	private Set<Category> categories;
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Imgur getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(Imgur profilepic) {
		this.profilepic = profilepic;
	}

	public Imgur getCoverpic() {
		return coverpic;
	}

	public void setCoverpic(Imgur coverpic) {
		this.coverpic = coverpic;
	}

	public Set<Category> getCategories() {
		if(null == categories) {
			categories = new HashSet<Category>();
		}
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
}
