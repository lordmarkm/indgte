package com.baldwin.indgte.pers‪istence.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.ChatMessage;

@Repository
@Transactional
public class ChatDaoImpl implements ChatDao {

	@Autowired
	private SessionFactory sessions;
	
	@Override
	public ChatMessage newMessage(String channel, String message) {
		ChatMessage chatMessage = new ChatMessage(channel, message);
		sessions.getCurrentSession().save(chatMessage);
		return chatMessage;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ChatMessage> getMessages(String[] channels, long lastReceivedId) {
		return sessions.getCurrentSession().createCriteria(ChatMessage.class)
			.add(Restrictions.gt(TableConstants.ID, lastReceivedId))
			.add(Restrictions.in(TableConstants.CHAT_CHANNEL, channels))
			.addOrder(Order.desc(TableConstants.ID))
			.list();
	}

}
