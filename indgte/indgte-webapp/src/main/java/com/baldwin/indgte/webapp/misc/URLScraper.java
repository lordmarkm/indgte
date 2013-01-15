package com.baldwin.indgte.webapp.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLScraper {
	static Logger log = LoggerFactory.getLogger(URLScraper.class);
	
	final static int THUMB_MINDIMENSION = 40; //pixels
	
	protected String url;
	protected Document doc;
	
	public URLScraper(String url) throws IOException {
		log.debug("Trying to get data from {}", url);
		this.url = formatUrl(url);
		this.doc = Jsoup.connect(this.url).get();		
		debugMetadata();
	}
	
	public String getTitle() {
		String title = null;
		
		Element descElement = doc.select("meta[name=og:title").first();
		if(null != descElement) {
			title = descElement.attr("content");
			if(title.length() > 0) return title;
		}
		
		return doc.title();
	}
	
	public String getDescription() {
		String description = null;
		
		Element descElement = doc.select("meta[name=description]").first();
		if(null != descElement) {
			description = descElement.attr("content");
			if(description.length() > 0) return description;
		}
		
		descElement = doc.select("meta[name=og:description]").first();
		if(null != descElement) {
			description = descElement.attr("content");
			if(description.length() > 0) return description;
		}
		
		return description;
	}
	
	public String getOgImage() {
		Element descElement = doc.select("meta[property=og:image]").first();
		
		if(null == descElement) {
			descElement = doc.select("meta[itemprop=image]").first();
		}
		
		if(null != descElement) {
			String imageUrl = descElement.attr("content");
			return formatImageUrl(imageUrl);
		} 
		
		return null;
	}
	
	public List<String> getImageUrls() {
		List<String> imageUrls = new ArrayList<>();
		for(Iterator<Element> i = doc.select("img").iterator(); i.hasNext();) {
			Element img = i.next();
			imageUrls.add(formatImageUrl(img.attr("src")));
		}
		
		return imageUrls;
	}
	
	public void debugMetadata() {
		Elements metadata = doc.select("meta");
		for(Iterator<Element> i = metadata.iterator(); i.hasNext();) {
			Element metadatum = i.next();
			log.debug("Meta element {}", metadatum);
			//log.debug("Meta name=[{}], content=[{}]", metadatum.attr("name"), metadatum.attr("content"));
		}
	}
	
	static final String HTTP = "http";
	static final String HTTP_PREFIX = "http://";
	private String formatUrl(String url) {
		if(null == url) return null;
		if(!url.startsWith(HTTP)) {
			url = HTTP_PREFIX + url;
		}
		return url;
	}
	
	private String formatImageUrl(String imageUrl) {
		if(imageUrl.startsWith("//")) {
			//e.g. //dgte.com/resources/images/image.jpg
			imageUrl = "http:" + imageUrl;
		} else if(imageUrl.startsWith("/")) {
			//e.g. /resources/images/image.jpg
			imageUrl = this.url + imageUrl;
		}
		
		return imageUrl;
	}

}
