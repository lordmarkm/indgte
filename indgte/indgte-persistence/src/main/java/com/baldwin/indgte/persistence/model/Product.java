package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.List;

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
import org.hibernate.search.annotations.Store;

import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summarizable;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Indexed
@Entity
@Table(name="products")
public class Product implements Summarizable {
	
	public static final String[] searchableFields = new String[]{"name", "description"};
	
	@Id @GeneratedValue @Column(name="productId")
	private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="categoryId", nullable=false, updatable=true)
	private Category category;

	@Field(store = Store.YES)
	@Column
	private String name;

	@Field
	@Column
	@Lob @Basic(fetch=FetchType.EAGER)
	private String description;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="mainpicId")
	private Imgur mainpic;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinTable(
		name="productPics",
		joinColumns = @JoinColumn(name="imageId"),
		inverseJoinColumns = @JoinColumn(name="productId")
	)
	private List<Imgur> pics;

	@Override
	public String toString() {
		return name + ": " + description;
	}

	@Override
	public Summary summarize() {
		return new Summary(SummaryType.product, id, name, description, category.getBusiness().getDomain() + "/" + id, mainpic == null ? null : mainpic.getHash());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	@JsonIgnore
	public List<Imgur> getPics() {
		if(null == pics) {
			pics = new ArrayList<Imgur>();
		}
		return pics;
	}

	public void setPics(List<Imgur> pics) {
		this.pics = pics;
	}

}