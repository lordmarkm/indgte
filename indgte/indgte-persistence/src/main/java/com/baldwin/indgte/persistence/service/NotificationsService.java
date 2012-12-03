package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.pers‪istence.dao.NotificationsDao;

@Service
public class NotificationsService {
	@Autowired
	private NotificationsDao dao;
	
	public Collection<Notification> getUnread(String username, long lastNotifId) {
		return dao.getUnread(username, lastNotifId);
	}

	public void clearNotifications(String username, Long... notificationId) {
		dao.clearNotifications(username, notificationId);
	}

	public Collection<Notification> getOldNotifs(String name, int start, int howmany) {
		return dao.getOldNotifs(name, start, howmany);
	}

	public CommentNotification commentNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		return dao.commentNotif(name, type, targetId, providerUserId, providerUsername);
	}

	public void delete(String username, Long[] notifIds) {
		dao.delete(username, notifIds);
	}

	public LikeNotification likeNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		return dao.likeNotif(name, type, targetId, providerUserId, providerUsername);
	}
}
