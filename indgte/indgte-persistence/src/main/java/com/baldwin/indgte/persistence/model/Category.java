package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="productCategories")
public class Category {
	@Id @GeneratedValue @Column(name="categoryId")
	private long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="businessId", nullable=false, updatable=false)
	private BusinessProfile business;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="category")
	private Set<Product> products;

	@Override
	public String toString() {
		return description;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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