package com.baldwin.indgte.pers‪istence.dao;

import java.util.Collection;

import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.NewBidNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.ReviewNotification;
import com.baldwin.indgte.persistence.model.ReviewReactNotification;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenVoteNotification;
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

	public void clearNotifications(String username, Long... ids);

	public Collection<Notification> getOldNotifs(String name, int start, int howmany);

	public CommentNotification commentNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername);

	public LikeNotification likeNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername);
	
	public void delete(String username, Long[] notifIds);

	public ReviewReactNotification reviewReactNotif(String reactorName, String mode, Review review);

	public ReviewNotification reviewNotif(Review review);

	public Collection<TopTenVoteNotification> topTenVote(TopTenCandidate candidate);

	public Collection<NewBidNotification> newBid(long itemId);

	public void commentRemove(InteractableType type, long targetId);

	public void unlike(InteractableType type, long targetId);
}
