package com.baldwin.indgte.persistence.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.BuyAndSellMode;

/**
 * An item on the buy-and-sell market
 * @author mbmartinez
 */

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@Column
	private long views;
	
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
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
		BuyAndSellItem other = (BuyAndSellItem) obj;
		if (id != other.id)
			return false;
		return true;
	}
}