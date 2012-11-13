package com.baldwin.indgte.webapp.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.AttachmentType;
import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.constants.ReviewType;
import com.baldwin.indgte.persistence.constants.WishType;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.webapp.dto.TopTenForm;

/**
 * For things like likes, posts and other stuff
 * I can't think of a better name than InteractiveController, not because
 * there is no better name, but because I suck
 * @author mbmartinez
 */

@Controller
@RequestMapping("/i/")
public interface InteractiveController {
	/**
	 * Get most recent posts of entities principal is subscribed to
	 */
	@RequestMapping(value = "/subposts.json", method = RequestMethod.GET)
	public JSON subposts(Principal principal, int start, int howmany);
	
	/**
	 * Get most recent posts of a single entity
	 */
	@RequestMapping(value = "/posts/", method = RequestMethod.GET)
	public JSON lastPosts(long posterId, PostType type, int start, int howmany);
	
	/**
	 * Post new status - supports attachments
	 * 
	 * image:
	 *  hash - imgur hash 
	 *  
	 * video:
	 * 	embed - embed code, 3rd party provided (explain this in the docs)
	 */
	@RequestMapping(value = "/newstatus.json", method = RequestMethod.POST)
	public JSON newstatus(Principal principal, WebRequest request);
	
	@RequestMapping(value = "/linkpreview/", method = RequestMethod.GET)
	public JSON linkpreview(String uri) throws IOException;
	
	/**
	 * New entity-specific posts? These don't support attachments and will soon be deprecated
	 */
	@RequestMapping(value = "/posts/", method = RequestMethod.POST)
	public JSON post(long posterId, PostType type, String title, String text);

	@RequestMapping(value = "/posts/business/", method = RequestMethod.POST)
	public JSON businessPost(long posterId, String title, String text);
	
	/**
	 * Subscribe/unsubscribe
	 */
	@RequestMapping(value = "/subscribe/{type}/{id}.json", method = RequestMethod.POST)
	public JSON subscribe(Principal principal, PostType type, Long id);
	
	@RequestMapping(value = "/unsubscribe/{type}/{id}.json", method = RequestMethod.POST)
	public JSON unsubscribe(Principal principal, PostType type, Long id);
	
	/**
	 * Review
	 */
	@RequestMapping(value = "/review/{type}/{targetId}.json", method = RequestMethod.GET)
	public JSON getReview(Principal principal, ReviewType type, long targetId);

	@RequestMapping(value = "/review/{type}/{targetId}.json", method = RequestMethod.POST)
	public JSON review(Principal principal, ReviewType type, long targetId, int score, String justification);
	
	@RequestMapping(value = "/allreviews/{type}/{targetId}.json", method = RequestMethod.GET)
	public JSON getAllReviews(Principal principal, ReviewType type, long targetId);
	
	@RequestMapping(value = "/reviewqueue.json", method = RequestMethod.GET)
	public JSON getReviewQueue(Principal principal);
	
	/**
	 * <b>Temporarily</b> remove the please review notif from the sidebar. If user views the business
	 * again the notif will return. TODO: should "never review" be supported? update: yes
	 */
	@RequestMapping(value = "/noreview/{businessId}.json", method = RequestMethod.POST)
	public JSON noReview(Principal principal, long businessId);
	
	@RequestMapping(value = "/neverreview/{businessId}.json", method = RequestMethod.POST)
	public JSON neverReview(Principal principal, long businessId);
	
	/*
	 * Top tens
	 */
	
	/**
	 * Show 5 popular, 5 recent + all the ones user created?
	 */
	@RequestMapping(value = "/toptens/", method = RequestMethod.GET)
	public ModelAndView toptens(Principal principal);
	
	@RequestMapping(value = "/toptens/", method = RequestMethod.POST)
	public ModelAndView newTopTenList(Principal principal, TopTenForm form);
	
	@RequestMapping(value = "/toptens.json", method = RequestMethod.GET)
	public JSON getTopTens(Principal principal);
	
	@RequestMapping(value = "/toptens/{toptenId}", method = RequestMethod.GET)
	public ModelAndView topten(Principal principal, long toptenId);
	
	@RequestMapping(value = "/toptens/businessgroup/{groupId}", method = RequestMethod.GET)
	public ModelAndView toptenBusiness(Principal principal, long groupId);
	
	@RequestMapping(value = "/toptens/{topTenId}.json", method = RequestMethod.POST)
	public JSON newTopTenCandidate(Principal principal, long topTenId, String title);
	
	@RequestMapping(value = "/toptens/{topTenId}/{candidateId}.json")
	public JSON vote(Principal principal, long topTenId, long candidateId);
	
	@RequestMapping(value = "/toptens/attach/{candidateId}.json", method = RequestMethod.POST)
	public JSON attachImageToCandidate(Principal principal, long candidateId, Imgur imgur);
	
	@RequestMapping(value = "/toptens/description/{candidateId}.json", method = RequestMethod.POST)
	public JSON addDescriptionToCandidate(Principal principal, long candidateId, String description);
	
	@RequestMapping(value = "/toptens/listdescription/{listId}.json", method = RequestMethod.POST)
	public JSON addDescriptionToList(Principal principal, long listId, String description);
	
	@RequestMapping(value = "/toptens/attachcandidate/{listId}/{type}/{attachmentId}", method = RequestMethod.GET)
	public ModelAndView attachCandidate(Principal principal, long listId, AttachmentType type, long attachmentId);
	
	/*
	 * Wishlists
	 */
	@RequestMapping(value = "/wishlist/{type}/{id}.json", method = RequestMethod.POST)
	public JSON addToWishlist(Principal principal, WishType type, long id);
}
