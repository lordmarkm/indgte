package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baldwin.indgte.persistence.dto.Summary;

@Entity
@Table(name="notifications_messages")
@Inheritance(strategy = InheritanceType.JOINED)
public class MessageNotification extends Notification {
	
	@ManyToOne(optional=false)
	@JoinColumn(name="senderId", updatable=false, nullable=false)
	private UserExtension sender;
	
	@Column
	private String channel;

	/**
	 * for when the same person sends multiple unread messages
	 */
	@Column
	private int howmany = 0;
	
	public NotificationType getType() {
		return NotificationType.message;
	}
	
	public Summary getSenderSummary() {
		return sender.summarize();
	}
	
	@JsonIgnore
	public UserExtension getSender() {
		return sender;
	}

	public void setSender(UserExtension sender) {
		this.sender = sender;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getHowmany() {
		return howmany;
	}

	public void setHowmany(int howmany) {
		this.howmany = howmany;
	}
}
