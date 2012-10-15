package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

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

	@Version
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
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

	public List<Imgur> getPics() {
		if(null == pics) {
			pics = new ArrayList<Imgur>();
		}
		return pics;
	}

	public void setPics(List<Imgur> pics) {
		this.pics = pics;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}