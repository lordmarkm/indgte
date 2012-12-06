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
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;

@Entity
@Table(name="posts")
public class Post {
	@Id	@GeneratedValue @Column(name="postId")
	private long id;

	@Column
	private PostType type; //should really be posterType
	
	@Column
	private Long posterId;
	
	@Column
	private String posterIdentifier;
	
	@Column
	private String posterTitle;
	
	@Column
	private String posterImgurHash;
	
	@Column
	private SummaryType attachmentType;
	
	@Column
	private Long attachmentId;
	
	@Column(length=500)
	private String attachmentIdentifier;
	
	@Column
	private String attachmentTitle;
	
	@Column
	private String attachmentImgurHash;
	
	@Column
	@Lob @Basic
	private String attachmentDescription;
	
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
		return title + ": " + text;
	}
	
	public Post() {
		//
	}
	
	public Post(Summary poster, Summary attachment) {
		this.posterId = poster.getId();
		this.posterIdentifier = poster.getIdentifier();
		this.posterTitle = poster.getTitle();
		this.posterImgurHash = poster.getThumbnailHash();
		if(null != attachment) {
			this.attachmentType = attachment.getType();
			this.attachmentId = attachment.getId();
			this.attachmentIdentifier = attachment.getIdentifier();
			this.attachmentTitle = attachment.getTitle();
			this.attachmentDescription = attachment.getDescription();
			this.attachmentImgurHash = attachment.getThumbnailHash();
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getPosterId() {
		return posterId;
	}

	public void setPosterId(Long posterId) {
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

	public String getPosterTitle() {
		return posterTitle;
	}

	public void setPosterTitle(String posterTitle) {
		this.posterTitle = posterTitle;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentTitle() {
		return attachmentTitle;
	}

	public void setAttachmentTitle(String attachmentTitle) {
		this.attachmentTitle = attachmentTitle;
	}

	public SummaryType getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(SummaryType attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getPosterImgurHash() {
		return posterImgurHash;
	}

	public void setPosterImgurHash(String posterImgurHash) {
		this.posterImgurHash = posterImgurHash;
	}

	public String getAttachmentImgurHash() {
		return attachmentImgurHash;
	}

	public void setAttachmentImgurHash(String attachmentImgurHash) {
		this.attachmentImgurHash = attachmentImgurHash;
	}

	public String getPosterIdentifier() {
		return posterIdentifier;
	}

	public void setPosterIdentifier(String posterIdentifier) {
		this.posterIdentifier = posterIdentifier;
	}

	public String getAttachmentIdentifier() {
		return attachmentIdentifier;
	}

	public void setAttachmentIdentifier(String attachmentIdentifier) {
		this.attachmentIdentifier = attachmentIdentifier;
	}

	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}
}
