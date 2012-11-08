package com.baldwin.indgte.persistence.model;

import static com.baldwin.indgte.persistence.constants.AttachmentType.none;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.dto.Attachable;
import com.baldwin.indgte.persistence.dto.Summary;

@Entity
@Table(name="toptencandidates")
public class TopTenCandidate implements Comparable<TopTenCandidate> {
	@Id
	@GeneratedValue
	@Column(name = "itemId")
	private long id;
	
	@ManyToOne
	@JoinColumn(
		name = "listId", 
		nullable = false,
		updatable = false
	)
	private TopTenList list;
	
	@ManyToOne
	@JoinColumn(
		name = "creatorId", 
		nullable=false, 
		updatable=false
	)
	private UserExtension creator;
	
	@Column
	private int votes;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="toptenvotes",
		joinColumns = {@JoinColumn(name = "userId")},
		inverseJoinColumns = {@JoinColumn(name = "candidateId")}
	)
	private Set<UserExtension> voters;
	
	@Column
	private String title;
	
	/* Attachment */
	
	@Column
	@Enumerated
	private AttachmentType attachmentType = none;
	
	@Column
	private Long attachmentId;

	@Transient
	private Attachable attachment;
	
	public TopTenCandidate() {
		//
	}
	
	public TopTenCandidate(Attachable attachment) {
		this.attachment = attachment;
		this.attachmentType = attachment.getAttachmentType();
		this.attachmentId = attachment.getId();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(null != attachment) {
			sb.append(attachment.getName());
		} else {
			sb.append(title);
		}
		sb.append("-" + votes);
		return sb.toString();
	}
	
	@Override
	public int compareTo(TopTenCandidate competition) {
		return competition.votes - this.votes;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	public TopTenList getList() {
		return list;
	}

	public void setList(TopTenList list) {
		this.list = list;
	}

	@JsonIgnore
	public Set<UserExtension> getVoters() {
		if(null == voters) {
			voters = new HashSet<UserExtension>();
		}
		return voters;
	}

	public void setVoters(Set<UserExtension> voters) {
		this.voters = voters;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public UserExtension getCreator() {
		return creator;
	}

	public void setCreator(UserExtension creator) {
		this.creator = creator;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public AttachmentType getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(AttachmentType attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	@JsonIgnore
	public Attachable getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachable attachment) {
		this.attachment = attachment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attachmentId == null) ? 0 : attachmentId.hashCode());
		result = prime * result
				+ ((attachmentType == null) ? 0 : attachmentType.hashCode());
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
		TopTenCandidate other = (TopTenCandidate) obj;

		//added this, different Candidate.id, same attachment returns true. Prevent double adding of entity to 
		//same topten list
		if(this.attachmentType != none) {
			if(this.attachmentId == other.attachmentId && this.attachmentType == other.attachmentType)
				return true;
		}
		
		if (attachmentId == null) {
			if (other.attachmentId != null)
				return false;
		} else if (!attachmentId.equals(other.attachmentId))
			return false;
		if (attachmentType != other.attachmentType)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	public Summary getAttachmentSummary() {
		return null == attachment ? null : attachment.summarize();
	}
}
