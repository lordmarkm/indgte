package com.baldwin.indgte.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A class to hold auxiliary user info
 * @author mbmartinez
 */

@Entity
@Table(name="userextensions")
public class UserExtension {
	@Id
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL, optional=false)
	@MapsId
	@JoinColumn(name="userId", nullable=false, unique=true)
	private User user;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="wisher")
	private Wishlist wishlist;

	public UserExtension(){
		//
	}
	
	public UserExtension(User user) {
		this.user = user;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}
}