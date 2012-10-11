package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="products")
public class Product {
	@Id @GeneratedValue @Column(name="productId")
	private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="categoryId", nullable=false, updatable=true)
	private Category category;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@OneToOne
	@JoinColumn(name="mainpicId")
	private Imgur mainpic;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinTable(
		name="productPics",
		joinColumns = @JoinColumn(name="imageId"),
		inverseJoinColumns = @JoinColumn(name="productId")
	)
	private Set<Imgur> pics;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public Set<Imgur> getPics() {
		if(null == pics) {
			pics = new HashSet<Imgur>();
		}
		return pics;
	}

	public void setPics(Set<Imgur> pics) {
		this.pics = pics;
	}
}