package com.baldwin.indgte.pers‪istence.dao;

import static com.baldwin.indgte.pers‪istence.dao.TableConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.dto.Attachable;
import com.baldwin.indgte.persistence.model.AuctionItem;
import com.baldwin.indgte.persistence.model.Bid;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.ChatMessage;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.NewBidNotification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.MessageNotification;
import com.baldwin.indgte.persistence.model.Notification;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.ReviewNotification;
import com.baldwin.indgte.persistence.model.ReviewReactNotification;
import com.baldwin.indgte.persistence.model.ReviewReactNotification.ReactionMode;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenVoteNotification;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.model.UserReview;

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
	
	@Autowired
	private FameDao fame;
	
	@Autowired
	private TradeDao trade;
	
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
		UserExtension commenter = users.getExtended(name, false);
		String commenterName = commenter != null ? commenter.getUsername() : name;
		
		switch(commentableType) {
		case post:
			return processPostComment(targetId, commenterName, providerUserId);
		default:
			throw new IllegalStateException("Unsupported comment location: " + commentableType);
		}
	}
	
	@Override
	public void commentRemove(InteractableType type, long targetId) {
		switch(type) {
		case post:
			Post post = interact.getPost(targetId);
			int newcomments = post.getComments() - 1;
			post.setComments(newcomments < 0 ? 0 : newcomments);
			break;
		default:
			throw new IllegalStateException("Unsupported comment location: " + type);
		}
	}
	
	@Override
	public LikeNotification likeNotif(String name, InteractableType type, long targetId, String providerUserId, String providerUsername) {
		UserExtension liker = users.getExtended(name, false);
		String likerName = liker != null ? liker.getUsername() : name;
		
		switch(type) {
		case post:
			return processPostLike(targetId, likerName, providerUserId);
		default:
			throw new IllegalStateException("Unsupported like type: " + type);
		}
	}
	
	@Override
	public void unlike(InteractableType type, long targetId) {
		switch(type) {
		case post:
			Post post = interact.getPost(targetId);
			int newlikes = post.getLikes() - 1;
			post.setLikes(newlikes < 0 ? 0 : newlikes);
			break;
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
		
		Post post = interact.getPost(postId);
		
		if(null == notification) {
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
		
		//fame
		post.setLikes(post.getLikes() + 1);
		fame.computeFame(notification.getNotified());
		
		return notification;
	}
	
	private CommentNotification processPostComment(long postId, String commenterName, String commenterId) {
		log.debug("Creating notification for post with id {}, from commenter {}", postId, commenterName);
		
		Session session = sessions.getCurrentSession();
		
		Post post = interact.getPost(postId);
		
		CommentNotification notification = null;
		notification = (CommentNotification) session.createCriteria(CommentNotification.class)
				.add(Restrictions.eq(NOTIF_COMMENT_TYPE, InteractableType.post))
				.add(Restrictions.eq(NOTIF_COMMENTORLIKE_TARGETID, postId))
				.add(Restrictions.eq(NOTIF_SEEN, false))
				.uniqueResult();
				
		if(null == notification) {
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
		
		//fame
		post.setComments(post.getComments() + 1);
		fame.computeFame(notification.getNotified());
		
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

	@Override
	public ReviewReactNotification reviewReactNotif(
			String reactorName, 
			String modeString, 
			Review review 
	) {
		
		ReactionMode mode = ReactionMode.valueOf(modeString);
		
		ReviewReactNotification notif = (ReviewReactNotification) sessions.getCurrentSession().createCriteria(ReviewReactNotification.class)
				.add(Restrictions.eq("revieweeIdentifier", review.getRevieweeSummary().getIdentifier()))
				.add(Restrictions.eq("notified", review.getReviewer()))
				.add(Restrictions.eq("mode", mode))
				.add(Restrictions.eq(NOTIF_SEEN, false))
				.uniqueResult();
		
		if(null == notif) {
			notif = new ReviewReactNotification();
			notif.setMode(mode);
			notif.setNotified(review.getReviewer());
			notif.setReactors(reactorName);
			notif.setRevieweeTitle(review.getRevieweeSummary().getTitle());
			notif.setRevieweeIdentifier(review.getRevieweeSummary().getIdentifier());
			notif.setReviewType(review.getReviewType());
		} else {
			notif.setReactors(constructMultinameString(notif.getReactors(), reactorName));
		}
		
		UserExtension reactor = users.getExtended(reactorName);
		notif.setLastReactorImageUrl(reactor.getImageUrl());
		notif.setTime(new Date());
		sessions.getCurrentSession().saveOrUpdate(notif);
		
		return notif;
	}

	@Override
	public ReviewNotification reviewNotif(Review review) {
		ReviewType type = review.getReviewType();
		
		ReviewNotification notif = new ReviewNotification();
		
		switch(type) {
		case business:
			BusinessReview businessReview = (BusinessReview)review;
			BusinessProfile business = businessReview.getReviewed();
			notif.setNotified(business.getOwner());
			notif.setBusinessReview(businessReview);
			break;
		case user:
			UserReview userReview = (UserReview)review;
			UserExtension reviewee = userReview.getReviewee();
			notif.setNotified(reviewee);
			notif.setUserReview(userReview);
			break;
		default:
			throw new IllegalStateException("Unsupported review type: " + type);
		}
		
		notif.setReviewType(review.getReviewType());
		notif.setTime(new Date());
		sessions.getCurrentSession().save(notif); //always new

		return notif;
	}

	@Override
	public Collection<TopTenVoteNotification> topTenVote(TopTenCandidate candidate) {
		
		Session session = sessions.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<TopTenVoteNotification> notifs = (List<TopTenVoteNotification>) sessions.getCurrentSession().createCriteria(TopTenVoteNotification.class)
				.add(Restrictions.eq("topten", candidate.getList()))
				.list();
		
		List<UserExtension> needNewNotifs = new ArrayList<>();
		
		for(TopTenCandidate competitor : candidate.getList().getCandidates()) {
			needNewNotifs.addAll(competitor.getVoters());
		}
		
		for(TopTenVoteNotification oldnotif : notifs) {
			needNewNotifs.remove(oldnotif.getNotified());
		}
		
		for(UserExtension needsNewNotif : needNewNotifs) {
			TopTenVoteNotification newNotif = new TopTenVoteNotification();
			newNotif.setNotified(needsNewNotif);
			newNotif.setTopten(candidate.getList());
			notifs.add(newNotif);
		}
		
		String imageUrl = null;
		if(null != candidate.getAttachmentId()) {
			Attachable attachment = candidate.getAttachment();
			if(null != attachment.getImgur()) {
				imageUrl = attachment.getImgur().getSmallSquare();
			}
		}
		
		for(TopTenVoteNotification notif : notifs) {
			notif.setImageUrl(imageUrl);
			notif.setSeen(false);
			notif.setTime(new Date());
			session.saveOrUpdate(notif);
		}
		
		return notifs;
	}

	@Override
	public Collection<NewBidNotification> newBid(long itemId) {
		Session session = sessions.getCurrentSession();
		
		AuctionItem item = (AuctionItem) trade.get(itemId);

		@SuppressWarnings("unchecked")
		List<NewBidNotification> notifs = session.createCriteria(NewBidNotification.class)
				.add(Restrictions.eq("item", item))
				.list();
		
		Set<UserExtension> toNotify = new HashSet<>();
		toNotify.add(item.getOwner());
		for(Bid bid : item.getBids()) {
			toNotify.add(bid.getBidder());
		}
		
		for(NewBidNotification oldNotif : notifs) {
			toNotify.remove(oldNotif.getNotified());
		}
		
		for(UserExtension notifyMe : toNotify) {
			NewBidNotification notif = new NewBidNotification();
			notif.setItem(item);
			notif.setNotified(notifyMe);
			notifs.add(notif);
		}
		
		for(NewBidNotification notif : notifs) {
			notif.setTime(new Date());
			notif.setSeen(false);
			session.saveOrUpdate(notif);
		}
		
		return notifs;
	}

}
