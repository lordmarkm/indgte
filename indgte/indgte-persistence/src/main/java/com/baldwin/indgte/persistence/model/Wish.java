package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.WishType;

@Entity
@Table(name="wishes")
public class Wish {
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private long targetId;
	
	@Enumerated
	@Column
	private WishType type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="listId", nullable=false, updatable=false)
	private Wishlist list;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public WishType getType() {
		return type;
	}

	public void setType(WishType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Wishlist getList() {
		return list;
	}

	public void setList(Wishlist list) {
		this.list = list;
	}
}