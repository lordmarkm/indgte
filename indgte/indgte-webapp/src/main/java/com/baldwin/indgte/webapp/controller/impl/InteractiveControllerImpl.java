package com.baldwin.indgte.webapp.controller.impl;

import static com.baldwin.indgte.webapp.controller.MavBuilder.clean;
import static com.baldwin.indgte.webapp.controller.MavBuilder.render;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baldwin.indgte.persistence.constants.PostType;
import com.baldwin.indgte.persistence.dto.Summary;
import com.baldwin.indgte.persistence.dto.Summary.SummaryType;
import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BusinessReview;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Post;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.TopTenList;
import com.baldwin.indgte.persistence.model.User;
import com.baldwin.indgte.persistence.service.BusinessService;
import com.baldwin.indgte.persistence.service.InteractiveService;
import com.baldwin.indgte.persistence.service.UserService;
import com.baldwin.indgte.pers‪istence.dao.InteractiveDao;
import com.baldwin.indgte.webapp.controller.InteractiveController;
import com.baldwin.indgte.webapp.controller.JSON;
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
	
	@Override
	public @ResponseBody JSON subposts(Principal principal, @RequestParam int start, @RequestParam int howmany) {
		try {
			return JSON.ok().put("posts", interact.getSubposts(principal.getName(), start, howmany));
		} catch (Exception e) {
			log.error("Exception getting subposts", e);
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON lastPosts(long posterId, PostType type, int start, int howmany) {
		return JSON.ok().put("posts", postDao.getById(posterId, type, start, howmany));
	}

	@Override
	public @ResponseBody JSON newstatus(Principal principal, WebRequest request) {
		log.debug("Poster id: [{}]", request.getParameter("posterId"));
		log.debug("Poster type: [{}]", request.getParameter("posterType"));
		log.debug("Text: [{}]", request.getParameter("text"));
		
		log.debug("Attachment? {}", request.getParameter("attachmentType"));
		
		Summary poster;
		PostType postType = PostType.valueOf(request.getParameter("posterType"));
		switch(postType) {
		case user:
			User user = users.getFacebook(principal.getName());
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
		case image:
			attachment = new Summary(attachmentType, null, null, null, null, request.getParameter("hash"));
			break;
		case video:
			String embed = request.getParameter("embed");
			embed = Jsoup.clean(embed, DgteTagWhitelist.videos());
			attachment = new Summary(attachmentType, null, null, null, embed, null);
			break;
		case product:
			Product product = businesses.getProduct(Long.parseLong(request.getParameter("entityId")));
			attachment = product.summarize();
			break;
		case category:
			Category category = businesses.getCategory(Long.parseLong(request.getParameter("entityId")));
			attachment = category.summarize();
			break;
		case link:
			attachment = new Summary(attachmentType, null, null, null, request.getParameter("link"), null);
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
	}
	
	@Override
	public @ResponseBody JSON linkpreview(@RequestParam String uri) throws IOException {
		log.debug("Trying to create preview for {}", uri);
		
		URLScraper scraper = new URLScraper(uri);
		scraper.getMetadata();
		return JSON.ok();
	}
	
	@Override
	public @ResponseBody JSON post(long posterId, PostType type, String title, String text) {
		title = clean(title);
		text = clean(text);
		Post post = postDao.newPost(posterId, type, title, text);
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
	public @ResponseBody JSON getReview(Principal principal, @PathVariable long businessId) {
		try {
			BusinessReview review = interact.getReview(principal.getName(), businessId); 
			if(null == review) {
				return JSON.teapot();
			} else {
				return JSON.ok().put("review", review);
			} 
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
	
	@Override
	public @ResponseBody JSON review(Principal principal, @PathVariable long businessId, 
				@RequestParam int score, @RequestParam String justification) {
		try {
			BusinessReview review = interact.review(principal.getName(), businessId, score, Jsoup.clean(clean(justification), DgteTagWhitelist.simpleText()));
			return JSON.ok().put("review", review);
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public @ResponseBody JSON getAllReviews(Principal principal, @PathVariable long businessId) {
		try {
			Collection<BusinessReview> reviews = interact.getReviews(businessId);
			return JSON.ok().put("reviews", reviews);
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public ModelAndView toptens(Principal principal) {
		User user = users.getFacebook(principal.getName());
		Collection<TopTenList> recentLists = interact.getRecentToptens(0, 5);
		Collection<TopTenList> popularLists = interact.getPopularToptens(0, 5);
		Collection<TopTenList> userLists = interact.getUserToptens(principal.getName());
		
		return render(user, "toptenlists")
				.put("recentLists", recentLists)
				.put("popularLists", popularLists)
				.put("userLists", userLists)
				.mav();
	}

	@Override
	public ModelAndView topten(Principal principal, @PathVariable long toptenId) {
		User user = users.getFacebook(principal.getName());
		TopTenList topten = interact.getTopten(toptenId);
		
		return render(user, "toptenlist")
				.put("topten", topten)
				.mav();
	}

	@Override
	public @ResponseBody JSON createTopten(Principal principal, @PathVariable String title) {
		try {
			TopTenList topten = interact.createTopTenList(principal.getName(), title);
			return JSON.ok().put("topten", topten);
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}

	@Override
	public JSON vote(Principal principal, long toptenId, long candidateId) {
		try {
			interact.topTenVote(principal.getName(), toptenId);
			return JSON.ok();
		} catch (Exception e) {
			return JSON.status500(e);
		}
	}
}
