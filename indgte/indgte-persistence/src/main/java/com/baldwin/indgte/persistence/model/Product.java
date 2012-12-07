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

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.dto.Attachable;
import com.baldwin.indgte.persistence.dto.Searchable;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

/**
 * An item offered by a Business
 * @author mbmartinez
 */

@Indexed
@Entity
@Table(name="products")
public class Product implements Searchable, Attachable {
	
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

	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private List<Wish> wishes;
	
	@Column
	private int comments;
	
	@Column
	private int likes;
	
	@Column
	private int sends;
	
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

	@Override
	@JsonIgnore
	public String[] getSearchableFields() {
		return searchableFields;
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
		Product other = (Product) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public AttachmentType getAttachmentType() {
		return AttachmentType.product;
	}

	@Override
	@JsonIgnore
	public Imgur getImgur() {
		return mainpic;
	}

	@JsonIgnore
	public List<Wish> getWishes() {
		return wishes;
	}

	public void setWishes(List<Wish> wishes) {
		this.wishes = wishes;
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
}