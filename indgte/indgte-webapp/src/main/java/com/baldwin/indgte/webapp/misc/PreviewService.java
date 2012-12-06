package com.baldwin.indgte.webapp.misc;

import java.net.URL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baldwin.indgte.persistence.model.BusinessProfile;
import com.baldwin.indgte.persistence.model.UserExtension;
import com.baldwin.indgte.pers‪istence.dao.BusinessDao;
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
	
	public Preview preview(PreviewType type, String href) {
		switch(type) {
		case user:
			return makeUserPreview(href);
		case business:
			return makeBusinessPreview(href);
		}
		
		return null;
	}

	private Preview makeUserPreview(String href) {
		String username = extractId(href);
		UserExtension user = users.getExtended(username);
		
		Preview preview = new Preview();
		preview.setDescription(user.getRank());
		preview.setImage(user.getImageUrl());
		preview.setTitle(user.getUsername());
		preview.setType(PreviewType.user);
		
		String provider = user.getUser().getProviderId();
		switch(provider) {
		case "facebook":
			preview.setCover(getCoverFromFacebook(user.getUser().getProviderUserId()));
			break;
		case "twitter":
			preview.setCover(getCoverFromTwitter(user.getUser().getProviderId()));
			break;
		default:
			log.error("Unknown provider: {}", provider);
		}
		
		return preview;
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
		return new Cover();	
	}
	
	private Preview makeBusinessPreview(String href) {
		String domain = extractId(href);
		
		BusinessProfile business = businesses.get(domain);
		
		Preview preview = new Preview();
		preview.setDescription(business.getDescription());
		preview.setImage(business.getImgur().getSmallSquare());
		preview.setTitle(business.getFullName());
		preview.setType(PreviewType.business);
		
		Cover cover = new Cover();
		cover.setSource(business.getCoverpic() != null ? business.getCoverpic().getSmallSquare() : "");
		preview.setCover(cover);
		
		return preview;
	}
	
	private String extractId(String href) {
		return href.substring(href.lastIndexOf("/") + 1, href.length());
	}
}