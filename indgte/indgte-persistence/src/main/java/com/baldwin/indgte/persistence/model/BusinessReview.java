package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.ReviewType;

@Entity
@Table(name="businessReviews")
public class BusinessReview {
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(optional=false)
	@JoinColumn(name="reviewerId", nullable=false, updatable=false)
	private User reviewer;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="reviewedId", nullable=false, updatable=false)
	private BusinessProfile reviewed;
	
	@Column
	private int score;
	
	@Column
	@Lob @Basic
	private String justification;
	
	@Column
	@Enumerated
	private ReviewType type = ReviewType.user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public BusinessProfile getReviewed() {
		return reviewed;
	}

	public void setReviewed(BusinessProfile reviewed) {
		this.reviewed = reviewed;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public ReviewType getType() {
		return type;
	}

	public void setType(ReviewType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
