package com.baldwin.indgte.persistence.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baldwin.indgte.persistence.constants.PostType;

@Entity
@Table(name="posts")
public class Post {
	@Id	@GeneratedValue @Column(name="postId")
	private long id;
	
	@Column
	private long posterId;
	
	@Column
	private PostType type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date postTime;
	
	@Column
	private String title;
	
	@Column
	@Lob @Basic(fetch=FetchType.EAGER)
	private String text;

	@Override
	public String toString() {
		return title;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPosterId() {
		return posterId;
	}

	public void setPosterId(long posterId) {
		this.posterId = posterId;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
}
