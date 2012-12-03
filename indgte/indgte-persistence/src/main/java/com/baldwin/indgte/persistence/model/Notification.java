package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Notification {

	public enum NotificationType {
		message, 
		comment,
		like
	}
	
	public enum InteractableType {
		post,
		review,
		business,
		category,
		product,
		fixedpriceitem,
		auctionitem,
		tradeitem
	}
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private boolean seen = false;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="notifiedId", updatable=false, nullable=false)
	private UserExtension notified;

	@Column
	@Lob @Basic
	private String text;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	public abstract NotificationType getType();
	
	@JsonIgnore
	public UserExtension getNotified() {
		return notified;
	}

	public void setNotified(UserExtension notified) {
		this.notified = notified;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}