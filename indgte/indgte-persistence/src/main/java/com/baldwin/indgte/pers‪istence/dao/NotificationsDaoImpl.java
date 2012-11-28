package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.pers‪istence.dao.TableConstants.*;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.CommentNotification.CommentableType;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class NotificationsDaoImpl implements NotificationsDao {

	private static final String BROADCAST_CHANNEL_PREFIX = "#";
	private static final String CHANNEL_USERNAME_DELIMITER_REGEXP = "\\|";
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private InteractiveDao interact;
	
	@Autowired
	private SessionFactory sessions;
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Notification> getUnread(String username, long lastNotifId) {
		return (Collection<Notification>) sessions.getCurrentSession().createCriteria(Notification.class)
				.createAlias(TableConstants.NOTIF_NOTIFIED_USER, ALIAS_USER)
				.add(Restrictions.eq(ALIAS_USER_USERNAME, username))
				.add(Restrictions.eq(TableConstants.NOTIF_SEEN, false))
				.add(Restrictions.gt(TableConstants.ID, lastNotifId))
				.addOrder(Order.desc(TableConstants.TIME))
				.list();
	}

	private String extractTargetUsernames(String sendername, String channel) {
		if(channel.startsWith(BROADCAST_CHANNEL_PREFIX)) return null;
		
		String[] usernames = channel.split(CHANNEL_USERNAME_DELIMITER_REGEXP);
		for(String username : usernames) {
			if(!sendername.equals(username)) return username;
		}
		
		return null;
	}
	
	public Notification getNotification(long id) {
		return (Notification) sessions.getCurrentSession().get(Notification.class, id);
	}
	
	@Override
	public MessageNotification getMessageNotification(String channel, String targetName) {
		return (MessageNotification) sessions.getCurrentSession().createCriteria(MessageNotification.class)
			.add(Restrictions.eq(TableConstants.NOTIF_MSG_CHANNEL, channel))
			.createAlias(TableConstants.NOTIF_NOTIFIED_USER, ALIAS_USER)
			.add(Restrictions.eq(ALIAS_USER_USERNAME, targetName))
			.add(Restrictions.eq(TableConstants.NOTIF_SEEN, false))
			.uniqueResult();
	}
	
	@Override
	public MessageNotification newMessageNotification(UserExtension sender, ChatMessage chatMessage) {
		String sendername = chatMessage.getSender();
		String channel = chatMessage.getChannel();
		
		//targetName will be null if it's a broadcast channel like #dumaguete.
		//we only want to notify for p2p messages
		String targetName;
		if((targetName = extractTargetUsernames(sendername, channel)) != null) {
			MessageNotification notification = getMessageNotification(channel, targetName);
			
			if(null == notification) {
				notification = new MessageNotification();
				notification.setSender(sender);
				notification.setChannel(channel);
				notification.setHowmany(1);
				notification.setNotified(users.getExtended(targetName));
				notification.setTime(new Date());
				sessions.getCurrentSession().save(notification);
			} else {
				notification.setHowmany(notification.getHowmany() + 1);
				notification.setTime(new Date());
			}
			
			return notification;
		}
		return null;
	}
	
	@Override
	public void clearNotification(long id) {
		Notification notification = getNotification(id);
		notification.setSeen(true);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Notification> getOldNotifs(String username, int start, int howmany) {
		return sessions.getCurrentSession().createCriteria(Notification.class)
				.createAlias(NOTIF_NOTIFIED_USER, ALIAS_USER)
				.add(Restrictions.eq(ALIAS_USER_USERNAME, username))
				.add(Restrictions.eq(NOTIF_SEEN, true))
				.addOrder(Order.desc(TIME))
				.setFirstResult(start).setMaxResults(howmany)
				.list();
	}

	@Override
	public CommentNotification commentNotif(String name, CommentableType commentableType, long targetId, 	String providerUserId, String providerUsername) {
		UserExtension commenter = users.getExtended(name);
		String commenterName = commenter != null ? commenter.getUsername() : providerUsername;
		
		switch(commentableType) {
		case post:
			return processPostComment(targetId, commenterName);
		default:
			throw new IllegalStateException("Unsupported comment type: " + commentableType);
		}
	}
	
	private CommentNotification processPostComment(long postId, String commenterName) {
		Session session = sessions.getCurrentSession();
		
		Post post = interact.getPost(postId);
		UserExtension poster = users.getExtended(post.getPosterId());
		
		CommentNotification notification = null;

		notification = (CommentNotification) session.createCriteria(CommentNotification.class)
				.add(Restrictions.eq(NOTIF_COMMENT_TYPE, CommentableType.post))
				.add(Restrictions.eq(NOTIF_COMMENT_TARGETID, postId))
				.add(Restrictions.eq(NOTIF_SEEN, false))
				.uniqueResult();
				
		if(null == notification) {
			notification = new CommentNotification();
			notification.setCommentableType(CommentableType.post);
			notification.setCommenters(commenterName);
			notification.setNotified(poster);
			notification.setTargetId(postId);
		} else {
			notification.setCommenters(constructCommentersString(notification.getCommenters(), commenterName));
		}
		
		notification.setTime(new Date());
		session.saveOrUpdate(notification);
		
		return notification;
	}
	
	@Test
	public void test() {
		String p = null;
		
		p = c(p, "Mark Martinez");
		System.out.println(p);
		
		p = c(p, "martinezdescent");
		System.out.println(p);
		
		p = c(p, "Hesus Nazareno");
		System.out.println(p);
		
		p = c(p, "Hesus Nazareno");
		System.out.println(p);
		
		p = c(p, "martinezdescent");
		System.out.println(p);
		
		p = c(p, "Cecille B. Demille");
		System.out.println(p);
	}
	
	private String c(String p, String n) {
		return constructCommentersString(p, n);
	}
	
	private String constructCommentersString(String previous, String newname) {
		
		if(null == previous) {
			return newname;
		}
		
		String[] names = previous.split(",");
		boolean addname = true;
		for(String name : names) {
			if(name.equals(newname)) {
				addname = false;
				break;
			}
		}
		
		if(addname) {
			return newname + "," + previous;
		} else {
			return previous;
		}
	}
}
