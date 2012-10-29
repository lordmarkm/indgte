package com.baldwin.indgte.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baldwin.indgte.persistence.dto.Summary;

@Entity
@Table(name="toptencandidates")
public class TopTenCandidate {
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
	
	@ManyToMany
	@JoinTable(
		name="toptenvotes",
		joinColumns = {@JoinColumn(name = "userId")},
		inverseJoinColumns = {@JoinColumn(name = "candidateId")}
	)
	private Set<User> voters;
	
	private String title;
	
	private Imgur imgur;
	
	private Summary attachment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TopTenList getList() {
		return list;
	}

	public void setList(TopTenList list) {
		this.list = list;
	}

	public Set<User> getVoters() {
		if(null == voters) {
			voters = new HashSet<User>();
		}
		return voters;
	}

	public void setVoters(Set<User> voters) {
		this.voters = voters;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Imgur getImgur() {
		return imgur;
	}

	public void setImgur(Imgur imgur) {
		this.imgur = imgur;
	}

	public Summary getAttachment() {
		return attachment;
	}

	public void setAttachment(Summary attachment) {
		this.attachment = attachment;
	}
}
