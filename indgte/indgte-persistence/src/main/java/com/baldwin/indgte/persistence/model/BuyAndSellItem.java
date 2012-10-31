package com.baldwin.indgte.persistence.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.baldwin.indgte.persistence.constants.BuyAndSellMode;

@Entity
@Table(name="buyandsell")
@Inheritance(strategy = InheritanceType.JOINED)
public class BuyAndSellItem {
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="ownerId", nullable=false, updatable=false)
	private User owner;
	
	@Column
	private String name;
	
	@Lob @Basic
	@Column
	private String description;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="imgurId")
	private Imgur imgur;
	
	@Enumerated
	@Column(name="mode")
	private BuyAndSellMode buyAndSellMode;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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

	public Imgur getImgur() {
		return imgur;
	}

	public void setImgur(Imgur imgur) {
		this.imgur = imgur;
	}

	public BuyAndSellMode getBuyAndSellMode() {
		return buyAndSellMode;
	}

	public void setBuyAndSellMode(BuyAndSellMode buyAndSellMode) {
		this.buyAndSellMode = buyAndSellMode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}