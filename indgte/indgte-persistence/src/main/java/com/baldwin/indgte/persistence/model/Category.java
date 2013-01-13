package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.dto.Attachable;
import com.baldwin.indgte.persistence.dto.Searchable;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Indexed
@Entity
@Table(name="categories")
public class Category implements Searchable, Attachable {
	
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
	private List<Product> products;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="details.advertisedCategory")
	private List<BillingTransaction> transactions;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="category")
	private List<SidebarFeature> sidebarFeatures;
	
	@Column
	private int comments;
	
	@Column
	private int likes;
	
	@Column
	private int sends;
	
	@Override
	public String toString() {
		return name + ": " + description + " owner: " + business;
	}
	
	@Override
	public Summary summarize() {
		Summary s = new Summary(SummaryType.category, id, name, description, business.getDomain() + "/" + id, mainpic == null ? null : mainpic.getHash());
		s.setImgur(mainpic);
		return s;
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
	public List<Product> getProducts() {
		if(null == products) {
			products = new ArrayList<Product>();
		}
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Override
	@JsonIgnore
	public String[] getSearchableFields() {
		return searchableFields;
	}

	@Override
	public AttachmentType getAttachmentType() {
		return AttachmentType.category;
	}

	@Override
	public Imgur getImgur() {
		return mainpic;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getSends() {
		return sends;
	}

	public void setSends(int sends) {
		this.sends = sends;
	}

	@JsonIgnore
	public List<BillingTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<BillingTransaction> transactions) {
		this.transactions = transactions;
	}

	@JsonIgnore
	public List<SidebarFeature> getSidebarFeatures() {
		return sidebarFeatures;
	}

	public void setSidebarFeatures(List<SidebarFeature> sidebarFeatures) {
		this.sidebarFeatures = sidebarFeatures;
	}
}