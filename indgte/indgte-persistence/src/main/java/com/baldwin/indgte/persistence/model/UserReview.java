package com.baldwin.indgte.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.dto.Summary;

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

	@Column
	private int agreeCount = 0;
	
	@Column
	private int disagreeCount = 0;
	
	@Column
	private int comments = 0;
	
	@ManyToMany
	@JoinTable(
		name="userreviewagreers",
		joinColumns = {@JoinColumn(name="reviewId")},
		inverseJoinColumns = {@JoinColumn(name="userId")}
	)
	private Set<UserExtension> agreers;
	
	@ManyToMany
	@JoinTable(
		name="userreviewdisagreers",
		joinColumns = {@JoinColumn(name="reviewId")},
		inverseJoinColumns = {@JoinColumn(name="userId")}
	)
	private Set<UserExtension> disagreers;
	
	
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

	@JsonIgnore
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

	@JsonIgnore
	@Override
	public UserExtension getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserExtension reviewer) {
		this.reviewer = reviewer;
	}

	@Override
	public Summary getReviewerSummary() {
		return reviewer.summarize();
	}

	@Override
	public Summary getRevieweeSummary() {
		return reviewee.summarize();
	}
	
	@Override
	@JsonIgnore
	public Set<UserExtension> getAgreers() {
		if(null == agreers) {
			this.agreers = new HashSet<UserExtension>();
		}
		return agreers;
	}

	public void setAgreers(Set<UserExtension> agreers) {
		this.agreers = agreers;
	}
	
	@Override
	@JsonIgnore
	public Set<UserExtension> getDisagreers() {
		if(null == disagreers) {
			this.disagreers = new HashSet<UserExtension>();
		}
		return disagreers;
	}

	public void setDisagreers(Set<UserExtension> disagreers) {
		this.disagreers = disagreers;
	}
	
	@Override
	public int getAgreeCount() {
		return agreeCount;
	}

	@Override
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}

	@Override
	public int getDisagreeCount() {
		return disagreeCount;
	}

	@Override
	public void setDisagreeCount(int disagreeCount) {
		this.disagreeCount = disagreeCount;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}
}
