package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.pers‪istence.dao.TableConstants.*;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.UserExtension;

@Repository
@Transactional
public class NotificationsDaoImpl implements NotificationsDao {

	static Logger log = LoggerFactory.getLogger(NotificationsDaoImpl.class);
	
	private static final String BROADCAST_CHANNEL_PREFIX = "#";
	private static final String CHANNEL_USERNAME_DELIMITER_REGEXP = "\\|";
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private InteractiveDao interact;
	
	@Autowired
	private SessionFactory sessions;
	
	@Autowired
	private BusinessDao businesses;
	
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
	public void clearNotifications(String username, Long... ids) {
		int updatedrows = sessions.getCurrentSession().createQuery("update Notification set seen = true where id in :ids")
			.setParameterList("ids", ids)
			.executeUpdate();
		
		log.debug("{} has cleared {} notifications", username, updatedrows);
//		Notification notification = getNotification(id);
//		notification.setSeen(true);
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
	public CommentNotification commentNotif(String name, InteractableType commentableType, long targetId, 	String providerUserId, String providerUsername) {
		UserExtension commenter = users.getExtended(name);
		String commenterName = commenter != null ? commenter.getUsername() : providerUsername;
		
		switch(commentableType) {
		case post:
			return processPostComment(targetId, commenterName, providerUserId);
		default:
			throw new IllegalStateException("Unsupported comment type: " + commentableType);
		}
	}
	
	@Override
	public LikeNotification likeNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		UserExtension commenter = users.getExtended(name);
		String commenterName = commenter != null ? commenter.getUsername() : providerUsername;
		
		switch(type) {
		case post:
			return processPostLike(targetId, commenterName, providerUserId);
		default:
			throw new IllegalStateException("Unsupported like type: " + type);
		}
	}
	
	private LikeNotification processPostLike(long postId, String likerName, String likerId) {
		log.debug("Creating notification for post with id {}, from commenter {}", postId, likerName);
		
		Session session = sessions.getCurrentSession();
		
		LikeNotification notification = null;
		notification = (LikeNotification) session.createCriteria(LikeNotification.class)
				.add(Restrictions.eq(NOTIF_LIKE_TYPE, InteractableType.post))
				.add(Restrictions.eq(NOTIF_COMMENTORLIKE_TARGETID, postId))
				.add(Restrictions.eq(NOTIF_SEEN, false))
				.uniqueResult();
				
		if(null == notification) {
			Post post = interact.getPost(postId);
			UserExtension notified = null;
			
			switch(post.getType()) {
			case user:
				if(post.getPosterIdentifier().equals(likerName)) {
					return null;
				}
				notified = users.getExtended(post.getPosterIdentifier());
				break;
			case business:
				BusinessProfile business = businesses.get(post.getPosterIdentifier());
				if(business.getOwner().getUsername().equals(likerName)) {
					return null;
				}
				notified = business.getOwner();
				break;
			default:
				throw new IllegalStateException("Unsupported post type: " + post.getType());
			}
			
			notification = new LikeNotification();
			notification.setLikeableType(InteractableType.post);
			notification.setLikers(likerName);
			notification.setNotified(notified);
			notification.setTargetId(postId);
			notification.setTargetTitle(post.getTitle());
		} else {
			if(notification.getNotified().getUsername().equals(likerName)) {
				return null;
			}
			notification.setLikers(constructMultinameString(notification.getLikers(), likerName));
		}
		
		notification.setLastLikerId(likerId);
		notification.setTime(new Date());
		session.saveOrUpdate(notification);
		
		return notification;
	}
	
	private CommentNotification processPostComment(long postId, String commenterName, String commenterId) {
		log.debug("Creating notification for post with id {}, from commenter {}", postId, commenterName);
		
		Session session = sessions.getCurrentSession();
		
		CommentNotification notification = null;
		notification = (CommentNotification) session.createCriteria(CommentNotification.class)
				.add(Restrictions.eq(NOTIF_COMMENT_TYPE, InteractableType.post))
				.add(Restrictions.eq(NOTIF_COMMENTORLIKE_TARGETID, postId))
				.add(Restrictions.eq(NOTIF_SEEN, false))
				.uniqueResult();
				
		if(null == notification) {
			Post post = interact.getPost(postId);
			UserExtension notified = null;
			
			switch(post.getType()) {
			case user:
				if(post.getPosterIdentifier().equals(commenterName)) {
					return null;
				}
				notified = users.getExtended(post.getPosterIdentifier());
				break;
			case business:
				BusinessProfile business = businesses.get(post.getPosterIdentifier());
				if(business.getOwner().getUsername().equals(commenterName)) {
					return null;
				}
				notified = business.getOwner();
				break;
			default:
				throw new IllegalStateException("Unsupported post type: " + post.getType());
			}
			
			notification = new CommentNotification();
			notification.setCommentableType(InteractableType.post);
			notification.setCommenters(commenterName);
			notification.setNotified(notified);
			notification.setTargetId(postId);
			notification.setTargetTitle(post.getTitle());
		} else {
			if(notification.getNotified().getUsername().equals(commenterName)) {
				return null;
			}
			notification.setCommenters(constructMultinameString(notification.getCommenters(), commenterName));
		}
		
		notification.setLastCommenterId(commenterId);
		notification.setTime(new Date());
		session.saveOrUpdate(notification);
		
		return notification;
	}
	
	private String constructMultinameString(String previous, String newname) {
		
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

	@Override
	public void delete(String username, Long[] notifIds) {
		int deleted = sessions.getCurrentSession().createQuery("delete from Notification where id in :ids")
			.setParameterList("ids", notifIds)
			.executeUpdate();
		log.debug("{} old notifications deleted by {}", deleted, username);
	}
}
