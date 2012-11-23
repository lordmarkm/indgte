package com.baldwin.indgte.persistence.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="chat")
public class ChatMessage {
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private String sender;

	@Column
	private String senderImageUrl;
	
	@Column
	private String channel;
	
	@Column
	@Lob @Basic
	private String message;

	public ChatMessage() {
		//
	}
	
	public ChatMessage(String sender, String senderImageUrl, String channel, String message) {
		this.sender = sender;
		this.senderImageUrl = senderImageUrl;
		this.channel = channel;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderImageUrl() {
		return senderImageUrl;
	}

	public void setSenderImageUrl(String senderImageUrl) {
		this.senderImageUrl = senderImageUrl;
	}
}