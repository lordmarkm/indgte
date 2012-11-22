package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="chat")
public class ChatMessage {
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private String channel;
	
	@Column
	private String message;

	public ChatMessage() {
		//
	}
	
	public ChatMessage(String channel, String message) {
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
}