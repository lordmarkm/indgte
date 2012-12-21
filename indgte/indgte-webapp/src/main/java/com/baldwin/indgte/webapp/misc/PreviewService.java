package com.baldwin.indgte.webapp.misc;

import java.net.URL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.BuyAndSellItem;
import com.baldwin.indgte.persistence.model.Category;
import com.baldwin.indgte.persistence.model.Imgur;
import com.baldwin.indgte.persistence.model.Product;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
import com.baldwin.indgte.pers‪istence.dao.TradeDao;
import com.baldwin.indgte.pers‪istence.dao.UserDao;
import com.baldwin.indgte.webapp.dto.Preview;
import com.baldwin.indgte.webapp.dto.Preview.Cover;
import com.baldwin.indgte.webapp.dto.Preview.PreviewType;

@Service
public class PreviewService {

	static Logger log = LoggerFactory.getLogger(PreviewService.class);
	
	@Autowired
	private UserDao users;
	
	@Autowired
	private BusinessDao businesses;
	
	@Autowired
	private TradeDao trade;
	
	public Preview preview(PreviewType type, String href) {
		switch(type) {
		case user:
			return makeUserPreview(href);
		case business:
			return makeBusinessPreview(href);
		case category:
			return makeCategoryPreview(href);
		case product:
			return makeProductPreview(href);
		case buyandsellitem:
			return makeBasPreview(href);
		}
		
		return null;
	}

	private Preview makeUserPreview(String href) {
		String username = extractId(href);
		UserExtension user = users.getExtended(username);
		
		Preview preview = new Preview();
		preview.setImage(user.getImageUrl());
		preview.setTitle(user.getUsername());
		preview.setType(PreviewType.user);
		preview.setDescription(user.getRank().toString());
		preview.setCover(getUserCover(user));
		
		//chat
		preview.setP2pName(username);
		
		return preview;
	}
	
	private Cover getUserCover(UserExtension user) {
		String provider = user.getUser().getProviderId();
		switch(provider) {
		case "facebook":
			return getCoverFromFacebook(user.getUser().getProviderUserId());
		case "twitter":
			return getCoverFromTwitter(user.getUser().getProviderUserId());
		default:
			log.error("Unknown provider: {}", provider);
			return null;
		}
	}
	
	private Cover getCoverFromFacebook(String id) {
		try {
			URL url = new URL("https://graph.facebook.com/" + id + "?fields=cover");
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode obj = mapper.readTree(url.openConnection().getInputStream());
			
			JsonNode jsonCover = obj.findPath("cover");

			Cover cover = new Cover();
			cover.setSource(jsonCover.findPath("source").getValueAsText());
			cover.setOffsetY(jsonCover.findPath("offset_y").getIntValue());
			return cover;
		} catch (Exception e) {
			log.error("Could not get cover photo from Facebook", e);
			return new Cover();
		}
	}
	
	private Cover getCoverFromTwitter(String id) {
		//TODO Here we wait for spring soc sec to support twitter banners
		Cover cover = new Cover();
		cover.setSource("twitter");
		return cover;
	}
	
	private Preview makeBusinessPreview(String href) {
		String domain = extractId(href);
		
		BusinessProfile business = businesses.get(domain);
		
		Preview preview = new Preview();
		preview.setDescription(business.getDescription());
		preview.setImage(business.getImgur() == null ? "" : business.getImgur().getSmallSquare());
		preview.setTitle(business.getFullName());
		preview.setType(PreviewType.business);
		
		Cover cover = new Cover();
		cover.setSource(business.getCoverpic() != null ? business.getCoverpic().getSmallSquare() : "");
		preview.setCover(cover);
		
		//chat
		preview.setP2pName(business.getOwner().getUsername());
		preview.setBusinessChannel(domain);
		
		return preview;
	}
	
	private Preview makeCategoryPreview(String href) {
		Long id = Long.parseLong(extractId(href));
		
		Category category = businesses.getCategory(id);
		BusinessProfile business = category.getBusiness();
		
		Preview preview = new Preview();
		preview.setDescription(category.getDescription());
		preview.setImage(category.getImgur() == null ? "" : category.getImgur().getSmallSquare());
		preview.setTitle(category.getName());
		preview.setType(PreviewType.category);
		
		Cover cover = new Cover();
		cover.setSource(business.getCoverpic() != null ? business.getCoverpic().getSmallSquare() : "");
		preview.setCover(cover);
		
		//chat
		preview.setP2pName(business.getOwner().getUsername());
		preview.setBusinessChannel(business.getDomain());
		
		return preview;
	}
	
	private Preview makeProductPreview(String href) {
		Long id = Long.parseLong(extractId(href));
		
		Product product = businesses.getProduct(id);
		BusinessProfile business = product.getCategory().getBusiness();
		
		Preview preview = new Preview();
		preview.setDescription(product.getDescription());
		preview.setImage(product.getImgur() == null ? "" : product.getImgur().getSmallSquare());
		preview.setTitle(product.getName());
		preview.setType(PreviewType.product);
		
		Cover cover = new Cover();
		Imgur imgur = product.getCategory().getBusiness().getImgur();
		cover.setSource(imgur == null ? "" : imgur.getSmallSquare());
		preview.setCover(cover);
		
		//chat
		preview.setP2pName(business.getOwner().getUsername());
		preview.setBusinessChannel(business.getDomain());
		
		return preview;
	}
	
	private Preview makeBasPreview(String href) {
		Long id = Long.parseLong(extractId(href));
		
		BuyAndSellItem item = trade.get(id);
		
		Preview preview = new Preview();
		preview.setDescription(item.getDescription());
		preview.setImage(item.getImgur() == null ? "" : item.getImgur().getSmallSquare());
		preview.setTitle(item.getName());
		preview.setType(PreviewType.buyandsellitem);
		preview.setCover(getUserCover(item.getOwner()));

		//chat
		preview.setP2pName(item.getOwner().getUsername());
		
		return preview;
	}
	
	private String extractId(String href) {
		return href.substring(href.lastIndexOf("/") + 1, href.length());
	}
}
