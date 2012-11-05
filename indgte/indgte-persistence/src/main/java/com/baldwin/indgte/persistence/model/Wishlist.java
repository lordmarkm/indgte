package com.baldwin.indgte.persistence.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name="wishlists")
public class Wishlist {
	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(mappedBy="list")
	@OrderColumn(name="order")
	private List<Wish> wishes;
	
	@OneToOne
	@JoinColumn(name="userId")
	private UserExtension wisher;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Wish> getWishes() {
		if(null == wishes) {
			wishes = new ArrayList<Wish>();
		}
		return wishes;
	}

	public void setWishes(List<Wish> wishes) {
		this.wishes = wishes;
	}

	public UserExtension getWisher() {
		return wisher;
	}

	public void setWisher(UserExtension wisher) {
		this.wisher = wisher;
	}
}