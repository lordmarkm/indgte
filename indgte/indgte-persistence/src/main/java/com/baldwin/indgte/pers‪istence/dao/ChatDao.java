package com.baldwin.indgte.pers‪istence.dao;

import java.util.List;

import com.baldwin.indgte.persistence.model.ChatMessage;

public interface ChatDao {

	ChatMessage newMessage(String channel, String message);

	List<ChatMessage> getMessages(String[] channels, long lastReceivedId);

}
