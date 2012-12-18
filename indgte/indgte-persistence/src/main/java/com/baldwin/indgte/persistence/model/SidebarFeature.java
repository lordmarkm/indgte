package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Entity
@Table(name="sidebar_features")
public class SidebarFeature {
	
	@Id @GeneratedValue
	private long id;
	
	@Enumerated
	private SummaryType type;
	
	@ManyToOne
	@JoinColumn(name="businessId")
	private BusinessProfile business;
	
	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="basId")
	private BuyAndSellItem bas;

	@Temporal(TemporalType.DATE)
	private Date start;
	
	@Temporal(TemporalType.DATE)
	private Date end;
	
	@Transient
	public Summary getSummary() {
		
		switch(type) {
		case business:
			return business.summarize();
		case category:
			return category.summarize();
		case product:
			return product.summarize();
		case buyandsellitem:
			return bas.summarize();
		default:
			throw new IllegalStateException("Illegal attachment type: " + type);
		}
		
	}
	
	public BusinessProfile getBusiness() {
		return business;
	}

	public void setBusiness(BusinessProfile business) {
		this.business = business;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BuyAndSellItem getBas() {
		return bas;
	}

	public void setBas(BuyAndSellItem bas) {
		this.bas = bas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SummaryType getType() {
		return type;
	}

	public void setType(SummaryType type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}