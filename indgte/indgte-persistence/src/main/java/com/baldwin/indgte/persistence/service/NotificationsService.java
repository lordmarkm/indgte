package com.baldwin.indgte.persistence.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.NewBidNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.ReviewNotification;
import com.baldwin.indgte.persistence.model.ReviewReactNotification;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenVoteNotification;
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

	/**
	 * @return the Notification object, or null if no notif is needed (i.e. entity owner = commenter)
	 */
	public CommentNotification commentNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		return dao.commentNotif(name, type, targetId, providerUserId, providerUsername);
	}

	public void commentRemove(InteractableType type, long targetId) {
		dao.commentRemove(type, targetId);
	}
	
	public void delete(String username, Long[] notifIds) {
		dao.delete(username, notifIds);
	}
	
	/**
	 * @return the Notification object, or null if no notif is needed (i.e. entity owner = commenter)
	 */
	public LikeNotification likeNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		return dao.likeNotif(name, type, targetId, providerUserId, providerUsername);
	}

	public void unlike(InteractableType type, long targetId) {
		dao.unlike(type, targetId);
	}
	
	public ReviewReactNotification reviewReactNotif(String reactorName, String mode, Review review) {
		return dao.reviewReactNotif(reactorName, mode, review);
	}

	public ReviewNotification reviewNotif(Review review) {
		return dao.reviewNotif(review);
	}

	public Collection<TopTenVoteNotification> topTenVote(TopTenCandidate candidate) {
		return dao.topTenVote(candidate);
	}

	public Collection<NewBidNotification> newBid(long itemId) {
		return dao.newBid(itemId);
	}

}
