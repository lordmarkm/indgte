package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.List;

import com.baldwin.indgte.persistence.model.ChatMessage;

public interface ChatDao {

	Object[] newMessage(String sender, String channel, String message);

	List<ChatMessage> getMessages(String[] channels, long lastReceivedId);

	Collection<ChatMessage> getChannelMessages(String channel, int howmany);

}
