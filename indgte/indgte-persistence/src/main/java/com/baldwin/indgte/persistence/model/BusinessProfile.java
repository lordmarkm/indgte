package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summarizable;

@Indexed
@Entity
@Table(name="businesses")
public class BusinessProfile implements Summarizable {
	
	public static final String[] searchableFields = new String[]{"domain", "fullName", "description"};
	
	@Id @GeneratedValue @Column(name="business_id")
	private long id;
	
	@OneToOne
	@JoinColumn(name="categoryId")
	private BusinessGroup category;;
	
	@Column(nullable=false, unique=true)
	@Field(store = Store.YES)
	private String domain;

	@Field(store = Store.YES)
	@Column(nullable=false)
	private String fullName;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="profilepicId")
	private Imgur profilepic;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="coverpicId")
	private Imgur coverpic;
	
	@Column
	@Lob @Basic(fetch=FetchType.EAGER)
	@Field
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
	private Double latitude = 0d;
	
	@Column
	private Double longitude = 0d;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="business")
	private Set<Category> categories;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="reviewed")
	private Set<BusinessReview> reviews;
	
	@Override
	public String toString() {
		return domain + ":" + fullName;
	}

	@Override
	public Summary summarize() {
		return new Summary(Summary.SummaryType.business, 
				id,
				fullName, 
				description, 
				domain, 
				profilepic == null ? null : profilepic.getHash());
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

	@JsonIgnore
	public Set<Category> getCategories() {
		if(null == categories) {
			categories = new HashSet<Category>();
		}
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public BusinessGroup getCategory() {
		return category;
	}

	public void setCategory(BusinessGroup category) {
		this.category = category;
	}

	@Override
	@JsonIgnore
	public String[] getSearchableFields() {
		return searchableFields;
	}

	@JsonIgnore
	public Set<BusinessReview> getReviews() {
		if(null == reviews) {
			reviews = new HashSet<BusinessReview>();
		}
		return reviews;
	}

	public void setReviews(Set<BusinessReview> reviews) {
		this.reviews = reviews;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessProfile other = (BusinessProfile) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
