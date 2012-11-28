package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class ChatDaoImpl implements ChatDao {

	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private NotificationsDao notifications;
	
	/**
	 * [ChatMessage, MessageNotification]
	 */
	@Override
	public Object[] newMessage(String sendername, String channel, String message) {
		UserExtension sender = users.getExtended(sendername);
		
		ChatMessage chatMessage = new ChatMessage(sendername, sender.getImageUrl(), channel, message);
		sessions.getCurrentSession().save(chatMessage);
	
		MessageNotification notification = notifications.newMessageNotification(sender, chatMessage);
		
		return new Object[] {chatMessage, notification};
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

	@Override
	@SuppressWarnings("unchecked")
	public Collection<ChatMessage> getChannelMessages(String channel, int howmany) {
		return sessions.getCurrentSession().createCriteria(ChatMessage.class)
				.add(Restrictions.eq(TableConstants.CHAT_CHANNEL, channel))
				.addOrder(Order.desc(TableConstants.ID))
				.setMaxResults(howmany)
				.list();
	}

}
