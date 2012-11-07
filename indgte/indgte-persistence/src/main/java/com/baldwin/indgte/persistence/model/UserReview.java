package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.ReviewType;

/**
 * Review for any user (emphasis on buy and sell)
 * @author mbmartinez
 */

@Entity
@Table(name="userreviews")
public class UserReview implements Review {
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="revieweeId", nullable=false, updatable=false)
	private UserExtension reviewee;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="reviewerId", nullable=false, updatable=false)
	private UserExtension reviewer;
	
	@Column(nullable=false)
	private int score;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date time;
	
	@Column
	@Lob @Basic
	private String justification;

	@Override
	public ReviewType getReviewType() {
		return ReviewType.user;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserExtension getReviewee() {
		return reviewee;
	}

	public void setReviewee(UserExtension reviewee) {
		this.reviewee = reviewee;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public UserExtension getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserExtension reviewer) {
		this.reviewer = reviewer;
	}
}
