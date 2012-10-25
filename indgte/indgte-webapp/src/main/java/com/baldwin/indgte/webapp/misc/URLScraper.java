package com.baldwin.indgte.webapp.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLScraper {
	static Logger log = LoggerFactory.getLogger(URLScraper.class);
	
	protected URL url;
	
	public URLScraper(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public void getMetadata() throws IOException {
		URLConnection connection = url.openConnection();
		connection.connect();
		
		Map<String, List<String>> headers = connection.getHeaderFields();
		log.debug("Headers: {}", headers);
	}
}
