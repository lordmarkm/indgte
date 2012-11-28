package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.CommentNotification.CommentableType;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.UserExtension;

/**
 * Handles notifications. I had no idea how big the scope of notifications needed to be. :v
 * @author mbmartinez
 */

public interface NotificationsDao {

	public Collection<Notification> getUnread(String username, long lastNotifId);
	
	public MessageNotification getMessageNotification(String channel, String targetName);
	
	/**
	 * @return new or existing MessageNotification if p2p
	 * 			null if broadcast (e.g. #dumaguete, #sumc)
	 */
	public MessageNotification newMessageNotification(UserExtension sender, ChatMessage chatMessage);

	public void clearNotification(long id);

	public Collection<Notification> getOldNotifs(String name, int start, int howmany);

	public CommentNotification commentNotif(String name, CommentableType type, long targetId, String providerUserId, String providerUsername);
}
