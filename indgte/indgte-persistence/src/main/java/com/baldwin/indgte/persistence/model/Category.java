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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Indexed
@Entity
@Table(name="categories")
public class Category implements Summarizable {
	
	public static final String[] searchableFields = new String[]{"name", "description"};
	
	@Id @GeneratedValue @Column(name="categoryId")
	private long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="businessId", nullable=false, updatable=false)
	private BusinessProfile business;
	
	@Field
	@Column
	private String name;
	
	@Field
	@Column
	@Lob @Basic(fetch=FetchType.EAGER)
	private String description;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="mainpicId")
	private Imgur mainpic;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinTable(
		name="categoryPics",
		joinColumns = @JoinColumn(name="imageId"),
		inverseJoinColumns = @JoinColumn(name="categoryId")
	)
	private Set<Imgur> pics;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="category")
	private Set<Product> products;

	@Override
	public String toString() {
		return name + ": " + description + " owner: " + business;
	}
	
	@Override
	public Summary summarize() {
		return new Summary(SummaryType.category, id, name, description, business.getDomain() + "/" + id, mainpic == null ? null : mainpic.getHash());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	public BusinessProfile getBusiness() {
		return business;
	}

	public void setBusiness(BusinessProfile business) {
		this.business = business;
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

	public Imgur getMainpic() {
		return mainpic;
	}

	public void setMainpic(Imgur mainpic) {
		this.mainpic = mainpic;
	}

	public Set<Imgur> getPics() {
		if(null == pics) {
			pics = new HashSet<Imgur>();
		}
		return pics;
	}

	public void setPics(Set<Imgur> pics) {
		this.pics = pics;
	}
	
	@JsonIgnore
	public Set<Product> getProducts() {
		if(null == products) {
			products = new HashSet<Product>();
		}
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}