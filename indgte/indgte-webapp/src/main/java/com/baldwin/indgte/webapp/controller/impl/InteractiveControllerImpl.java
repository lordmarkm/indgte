package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.basicWithImages;
import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.redirect;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;
import static com.baldwin.indgte.webapp.misc.DgteConstants.REVIEW_PREVIEW_LENGTH;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.constants.Background;
import com.baldwin.indgte.persistence.constants.Initializable;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.Theme;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.CommentNotification;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.LikeNotification;
import com.baldwin.indgte.persistence.model.Notification.InteractableType;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.Review;
import com.baldwin.indgte.persistence.model.ReviewNotification;
import com.baldwin.indgte.persistence.model.ReviewReactNotification;
import com.baldwin.indgte.persistence.model.TopTenCandidate;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.TopTenVoteNotification;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.NotificationsService;
import com.baldwin.indgte.persistence.service.TradeService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.webapp.controller.InteractiveController;
import com.baldwin.indgte.webapp.controller.JSON;
import com.baldwin.indgte.webapp.controller.MavBuilder;
import com.baldwin.indgte.webapp.dto.TopTenForm;
import com.baldwin.indgte.webapp.misc.Comet;
import com.baldwin.indgte.webapp.misc.ConstantsInserterBean;
import com.baldwin.indgte.webapp.misc.DgteTagWhitelist;
import com.baldwin.indgte.webapp.misc.URLScraper;

@Component
public class InteractiveControllerImpl implements InteractiveController {

	static Logger log = LoggerFactory.getLogger(InteractiveControllerImpl.class);

	@Autowired
	private InteractiveService interact;

	@Autowired 
	private UserService users;

	@Autowired
	private BusinessService businesses;

	@Autowired
	private InteractiveDao postDao;

	@Autowired
	private NotificationsService notifs;
	
	@Autowired
	private TradeService trade;
	
	@Autowired
	private Comet comet;
	
	@Autowired
	private ConstantsInserterBean constants;
	
	@Override
	public @ResponseBody JSON subposts(Principal principal, @RequestParam int start, @RequestParam int howmany, @RequestParam String sort, boolean hasSticky) {
		try {
			return JSON.ok()
						.put("posts", interact.getPosts(principal == null ? null : principal.getName(), start,  howmany, sort))
						.put("featured", hasSticky ? null : interact.getRandomFeaturedPost());
			
		} catch (Exception e) {
			log.error("Exception getting subposts", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON lastPosts(long posterId, PostType type, int start, int howmany, boolean hasSticky) {
		return JSON.ok()
					.put("posts", postDao.getById(posterId, type, start, howmany))
					.put("featured", hasSticky ? null : interact.getRandomFeaturedPost());
	}

	@Override
	public ModelAndView viewpost(Principal principal, @PathVariable long postId) {
		Post post = interact.getPost(postId);

		MavBuilder mav = render("viewpost")
				.put("post", post)
				.description(post.getText());
		
		if(post.getAttachmentImgurHash() != null) {
			mav.thumbnail(Imgur.getSmallSquare(post.getAttachmentImgurHash()));
		}
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user)
			   .put("owner", interact.isPostOwner(post, user));
		}
		
		constants.insertConstants(mav);
		return mav.mav();
	}
	
	@Override
	public @ResponseBody JSON groupPosts(Principal principal, long groupId, int start, int howmany) {
		try {
			List<Post> posts = interact.getBusinessGroupPosts(groupId, start, howmany);
			Post featured = interact.getBusinessGroupFeaturedPost(groupId);
			return JSON.ok()
						.put("posts", posts)
						.put("featured", featured);
		} catch (Exception e) {
			log.error("Exception getting business group posts", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON newstatus(Principal principal, WebRequest request) {
		try {
			log.debug("Poster id: [{}]", request.getParameter("posterId"));
			log.debug("Poster type: [{}]", request.getParameter("posterType"));
			log.debug("Text: [{}]", request.getParameter("text"));
			log.debug("Attachment? {}", request.getParameter("attachmentType"));
	
			Summary poster;
			PostType postType = PostType.valueOf(request.getParameter("posterType"));
			switch(postType) {
			case user:
				UserExtension user = users.getExtended(principal.getName());
				poster = user.summarize();
				break;
			case business:
				BusinessProfile business = businesses.get(Long.parseLong(request.getParameter("posterId")));
				poster = business.summarize();
				break;
			default:
				throw new IllegalArgumentException("Illegal post type " + postType);
			}
	
			Summary attachment;
			SummaryType attachmentType = SummaryType.valueOf(request.getParameter("attachmentType"));
			switch(attachmentType) {
			case imgur:
				attachment = new Summary(attachmentType, null, null, null, null, request.getParameter("hash"));
				break;
			case video:
				String embed = request.getParameter("embed");
				embed = Jsoup.clean(embed, DgteTagWhitelist.videos());
				attachment = new Summary(attachmentType, null, null, null, embed, null);
				break;
			case business:
				log.debug("Attaching business with id {}", request.getParameter("entityId"));
				BusinessProfile business = businesses.get(Long.parseLong(request.getParameter("entityId")));
				attachment = business.summarize();
				break;
			case category:
				log.debug("Attaching category with id {}", request.getParameter("entityId"));
				Category category = businesses.getCategory(Long.parseLong(request.getParameter("entityId")));
				attachment = category.summarize();
				break;
			case product:
				log.debug("Attaching product with id {}", request.getParameter("entityId"));
				Product product = businesses.getProduct(Long.parseLong(request.getParameter("entityId")));
				attachment = product.summarize();
				break;
			case buyandsellitem:
				log.debug("Attaching buy and sell item with id {}", request.getParameter("entityId"));
				BuyAndSellItem bas = trade.get(null, Long.parseLong(request.getParameter("entityId")));
				attachment = bas.summarize();
				break;
			case link:
				String link = request.getParameter("link");
				log.debug("Trying to get data from {}", link);
				
				String linkTitle = request.getParameter("attachmentTitle");
				String linkDescription = request.getParameter("attachmentDescription");
				String linkUrl = request.getParameter("attachmentIdentifier");
				String imgUrl = request.getParameter("attachmentImgurHash");
	
				attachment = new Summary(attachmentType, null, linkTitle, linkDescription, linkUrl, imgUrl);
				break;
			case none:
				attachment = null;
				break;
			default:
				throw new IllegalArgumentException("Illegal attachment type " + attachmentType);
			}
	
			Post post = new Post(poster, attachment);
	
			String title = clean(request.getParameter("title"));
			String text = clean(request.getParameter("text"));
	
			post.setType(postType);
			post.setPostTime(new Date());
			post.setTitle(title);
			post.setText(text);
	
			interact.saveOrUpdate(post);
			log.debug("Post: {} text: {}", post);
	
			return JSON.ok().put("post", post);
		} catch (Exception e) {
			log.error("Error creating post.", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON deletepost(Principal principal, @PathVariable long id) {
		if(null == principal) {
			return JSON.status500(new IllegalArgumentException("You are not logged in."));
		}
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean moderator = isModerator(auth);
			interact.deletepost(moderator, principal.getName(), id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON linkpreview(@RequestParam String uri) throws IOException {
		log.debug("Trying to create preview for {}", uri);

		try {
			URLScraper scraper = new URLScraper(uri);
			return JSON.ok()
						.put("url", uri)
						.put("title", scraper.getTitle())
						.put("description", scraper.getDescription())
						.put("ogImage", scraper.getOgImage()) //open graph image, for FB optimization
						.put("images", scraper.getImageUrls());
		} catch (Exception e) {
			log.warn("Could not scrape {}, got exception {}", uri, e);
			return JSON.status500(e);
		}
		
	}

	@Override
	public @ResponseBody JSON post(long posterId, PostType type, String title, String text) {
		title = clean(title);
		text = clean(text);
		Post post = interact.newPost(posterId, type, title, text);
		return JSON.ok().put("post", post);
	}

	@Override
	public @ResponseBody JSON businessPost(long posterId, String title, String text) {
		return post(posterId, PostType.business, title, text);
	}

	@Override
	public @ResponseBody JSON subscribe(Principal principal, @PathVariable PostType type, @PathVariable Long id) {
		try {
			interact.subscribe(principal.getName(), type, id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON unsubscribe(Principal principal, @PathVariable PostType type, @PathVariable Long id) {
		try {
			interact.unsubscribe(principal.getName(), type, id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON countsubs(Principal principal, @PathVariable PostType type, @PathVariable Long id) {
		try {
			int subscount = interact.countsubs(type, id);
			return JSON.ok().put("subscount", subscount);
		} catch (Exception e) {
			log.error("Error", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON getReview(Principal principal, @PathVariable ReviewType type, @PathVariable long targetId) {
		try {
			Review review = null;
			switch(type) {
			case business:
				review = interact.getBusinessReview(principal.getName(), targetId);
				break;
			case user:
				review = interact.getUserReview(principal.getName(), targetId);
				break;
			}

			if(null != review) {
				return JSON.ok().put("review", review);
			} else {
				return JSON.teapot();
			}
		} catch (Exception e) {
			log.error("Exception getting review", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON review(Principal principal, 
			@PathVariable ReviewType type, 
			@PathVariable long targetId,	
			@RequestParam int score, 
			@RequestParam String justification) {
		try {
			Review review = null;
			switch(type) {
			case business:
				review = interact.businessReview(principal.getName(), targetId, score, Jsoup.clean(clean(justification), DgteTagWhitelist.simpleText()));
				break;
			case user:
				review = interact.userReview(principal.getName(), targetId, score, Jsoup.clean(justification, DgteTagWhitelist.simpleText()));
			}
			ReviewNotification notif = notifs.reviewNotif(review);
			comet.fireNotif(notif);
			
			return JSON.ok().put("review", review);
		} catch (Exception e) {
			log.error("Exception posting review", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getAllReviews(Principal principal, @PathVariable ReviewType type, @PathVariable long targetId) {
		log.debug("Getting all reviews for type {}, id {}", type, targetId);
		try {
			Collection<? extends Review> reviews = interact.getReviews(targetId, type);
			Object[] reviewStats = interact.getReviewStats(targetId, type);

			if(log.isDebugEnabled()) {
				log.debug("Found {} reviews. Review stats: {}", reviews.size(), Arrays.asList(reviewStats));
			}

			return JSON.ok()
					.put("reviews", reviews)
					.put("reviewCount", reviewStats[0])
					.put("averageScore", reviewStats[1]);
		} catch (Exception e) {
			log.error("Exception getting reviews", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getReviewQueue(Principal principal) {
		try {
			List<Summary> reviewRequests = interact.getReviewRequests(principal.getName());
			return JSON.ok().put("reviewqueue", reviewRequests);
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON noReview(Principal principal, @PathVariable long businessId) {
		try {
			interact.noReview(principal.getName(), businessId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON neverReview(Principal principal, @PathVariable long businessId) {
		try {
			interact.neverReview(principal.getName(), businessId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView toptens(Principal principal) {
		Collection<TopTenList> recentLists = interact.getRecentToptens(0, 8);
		Collection<TopTenList> popularLists = interact.getPopularToptens(0, 8);
		//Collection<TopTenList> userLists = interact.getUserToptens(principal.getName());
		Collection<TopTenList> allLists = interact.getAllLists();
		
		MavBuilder mav =  render("toptenlists")
				.put("recentLists", recentLists)
				.put("popularLists", popularLists)
				.put("allLists", allLists)
				.put("form", new TopTenForm());
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user);
		}
		
		return mav.mav();
	}

	@Override
	public @ResponseBody JSON getTopTens(Principal principal) {
		try{
			Collection<TopTenList> recentLists = interact.getRecentToptens(0, 3);
			Collection<TopTenList> popularLists = interact.getPopularToptens(0, 3);

			return JSON.ok()
					.put("recent", recentLists)
					.put("popular", popularLists);
		} catch (Exception e) {
			log.error("Exception getting toptens", e);
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView topten(Principal principal, @PathVariable long toptenId) {
		TopTenList topten = interact.getTopten(toptenId);
		TopTenCandidate userVoted = null;
		
//		if(log.isDebugEnabled()) {
//			StringBuilder candidateIds = new StringBuilder();
//			for(TopTenCandidate candidate : topten.getCandidates()) {
//				candidateIds.append(candidate.getId() + ",");
//			}
//		
//			StringBuilder votedIds = new StringBuilder();
//			for(TopTenCandidate candidate : user.getVotes()) {
//				votedIds.append(candidate.getId() + ",");
//			}
//			
//			log.debug("List candidate ids: {}", candidateIds);
//			log.debug("User voted ids: {}", votedIds);
//		}
		
		MavBuilder mav = render("toptenlist")
				.put("topten", topten);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName(), Initializable.toptenvotes);
	
			for(TopTenCandidate voted : user.getVotes()) {
				if(voted.getList().getId() == toptenId) {
					userVoted = voted;
					break;
				}
			}
			
			mav.put("user", user);
		}
		
		if(null != userVoted) {
			log.debug("User has voted for candidate with id {}", userVoted.getId());
		} else {
			log.debug("User has Not voted for a candidate in this list.");
		}
		
		return mav.put("userVoted", userVoted == null ? 0 : userVoted.getId())
				.mav();
	}

	@Override
	public ModelAndView toptenBusiness(Principal principal, @PathVariable long groupId) {
		long toptenId = interact.getBusinessTopTenListId(groupId);
		return redirect("/i/toptens/" + toptenId).mav();
	}

	@Override
	public ModelAndView newTopTenList(Principal principal, TopTenForm form) {
		//TopTenList topten = interact.createTopTenList(principal.getName(), Jsoup.clean(title, DgteTagWhitelist.none()));

		TopTenList topten = form.getList();
		interact.saveTopTenList(principal.getName(), topten);

		return redirect("/i/toptens/" + topten.getId()).mav();
	}

	@Override
	public @ResponseBody JSON newTopTenCandidate(Principal principal, @PathVariable long topTenId, @RequestParam String title) {
		try {
			String cleanTitle = Jsoup.clean(title, DgteTagWhitelist.none());
			TopTenCandidate candidate = interact.createTopTenCandidate(principal.getName(), topTenId, cleanTitle);
			if(null == candidate) {
				return JSON.status409("Attachment already exists in this list.");
			}
			return JSON.ok().put("candidate", candidate);
		} catch (Exception e) {
			log.error("Exception creating candidate", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON vote(Principal principal, @PathVariable long topTenId, @PathVariable long candidateId) {
		log.debug("User {} voting for candidate with id {}", principal.getName(), candidateId);
		try {
			TopTenCandidate candidate = interact.topTenVote(principal.getName(), candidateId);
			interact.initializeAttachment(candidate);
			
			Collection<TopTenVoteNotification> newnotifs = notifs.topTenVote(candidate);
			for(TopTenVoteNotification notif : newnotifs) {
				comet.fireNotif(notif);
			}
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception voting", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON attachImageToCandidate(Principal principal, @PathVariable long candidateId, Imgur imgur) {
		try {
			interact.attachImageToCandidate(principal.getName(), candidateId, imgur);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON addDescriptionToCandidate(Principal principal, @PathVariable long candidateId, @RequestParam("description") String description) {
		try {
			description = basicWithImages(description);
			return JSON.ok().put("imgur", interact.addDescriptionToCandidate(candidateId, description));
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON addDescriptionToList(Principal principal, @PathVariable long listId, @RequestParam("description") String description) {
		log.debug("Adding description {} to list with id {}", description, listId);
		try {
			description = basicWithImages(description);
			return JSON.ok().put("description", interact.addDescriptionToList(listId, description));
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public ModelAndView attachCandidate(Principal principal, @PathVariable long listId, @PathVariable AttachmentType type, @PathVariable long attachmentId) {
		interact.addEntityToList(principal.getName(), listId, type, attachmentId);
		return redirect("/i/toptens/" + listId).mav();
	}
	
	@Override
	public @ResponseBody JSON addToWishlist(Principal principal, @PathVariable WishType type, @PathVariable long id) {
		try {
			interact.addToWishlist(principal.getName(), type, id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception adding to wishlist", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON changetheme(Principal principal, @PathVariable Theme newtheme) {
		try {
			interact.changetheme(principal.getName(), newtheme);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON changebg(Principal principal, @PathVariable Background newbg) {
		try {
			interact.changebg(principal.getName(), newbg);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView viewReview(Principal principal, @PathVariable ReviewType type, @PathVariable long reviewId) {
		Review review = interact.getReview(type, reviewId, true);
		
		String thumbnail = review.getRevieweeSummary().getImgur() != null ? review.getRevieweeSummary().getImgur().getSmallSquare() : null;
		String pageDescription = review.getReviewer().getUsername() + "'s review of " + review.getRevieweeSummary().getTitle();
		String justification = review.getJustification();
		
		if(justification.length() > REVIEW_PREVIEW_LENGTH) {
			pageDescription += " - " + justification.substring(0, REVIEW_PREVIEW_LENGTH);
		} else {
			pageDescription += " - " + justification;
		}
		
		MavBuilder mav = render("viewreview")
				.put("review", review)
				.put(MavBuilder.PAGE_DESCRIPTION, pageDescription)
				.put(MavBuilder.PAGE_THUMBNAIL, thumbnail);
		constants.insertConstants(mav);
		
		if(null != principal) {
			UserExtension user = users.getExtended(principal.getName());
			mav.put("user", user)
			   .put("agree", review.getAgreers().contains(user))
			   .put("disagree", review.getDisagreers().contains(user));
		}
		
		return mav.mav();
	}

	@Override
	public @ResponseBody JSON deleteReview(Principal principal, @PathVariable ReviewType reviewType, @PathVariable long reviewId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean moderator = isModerator(auth);
			interact.deleteReview(moderator, principal.getName(), reviewType, reviewId);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error deleting review.", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON reviewReact(Principal principal, @PathVariable ReviewType type, @PathVariable String mode, @PathVariable long reviewId) {
		try {
			Review review = interact.reviewReact(principal.getName(), type, mode, reviewId);
			ReviewReactNotification notification = notifs.reviewReactNotif(principal.getName(), mode, review);
			comet.fireNotif(notification);
			return JSON.ok().put("review", review);
		} catch (Exception e) {
			log.error("Error that I forgot to log again", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON clearNotification(Principal principal, @PathVariable long id) {
		try {
			notifs.clearNotifications(principal.getName(), id);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error clearing unread notif", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getOldNotifs(Principal principal, @PathVariable int start, @PathVariable int howmany) {
		try {
			return JSON.ok().put("notifs", notifs.getOldNotifs(principal.getName(), start, howmany));
		} catch (Exception e) {
			log.error("Error getting old notifs", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON commentNotify(Principal principal, 
			@PathVariable InteractableType type, @PathVariable long targetId,
			@RequestParam String name,
			@RequestParam String providerUserId, 
			@RequestParam String providerUsername) {
		
		String commenterUsername = principal == null ? name : principal.getName();
		
		try {
			CommentNotification notif = notifs.commentNotif(commenterUsername, type, targetId, providerUserId, providerUsername);
			if(null != notif) comet.fireNotif(notif);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error adding comment notif", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON commentRemove(@PathVariable InteractableType type, @PathVariable long targetId) {
		log.debug("Someone has removed his comment from {} with id {}", type, targetId);
		try {
			notifs.commentRemove(type, targetId);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Ex", e);
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON likeNotify(Principal principal, 
			@PathVariable InteractableType type, @PathVariable long targetId, 
			@RequestParam String name,
			@RequestParam String providerUserId, 
			@RequestParam String providerUsername) {
		
		String likerName = principal == null ? name : principal.getName();
		
		try {
			LikeNotification notif = notifs.likeNotif(likerName, type, targetId, providerUserId, providerUsername);
			if(null != notif) comet.fireNotif(notif);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Error adding like notif", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON unlike(@PathVariable InteractableType type, @PathVariable long targetId) {
		log.debug("Someone has 'Unliked' {} with id {}", type, targetId);
		try {
			notifs.unlike(type, targetId);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Ex", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON deleteNotifs(Principal principal, @RequestParam("notifIds[]") Long[] notifIds) {
		try {
			notifs.delete(principal.getName(), notifIds);
			return JSON.ok();
		} catch (Exception e) {
			log.error("Exception deleting notifs", e);
			return JSON.status500(e);
		}
	}

	private boolean isModerator(Authentication auth) {
		log.debug("Checking authentication. Authorities: {}", auth.getAuthorities());
		for(GrantedAuthority g : auth.getAuthorities()) {
			if(g.getAuthority().equals("ROLE_MODERATOR")) return true;
		}
		return false;
	}
	
	@ExceptionHandler(Exception.class)
	public String error(Exception e) {
		log.error("Exception!", e);
		return "redirect:/error/";
	}

}
