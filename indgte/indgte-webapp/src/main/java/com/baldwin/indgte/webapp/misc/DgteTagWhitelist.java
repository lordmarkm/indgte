package com.baldwin.indgte.webapp.misc;

import org.jsoup.safety.Whitelist;

public class DgteTagWhitelist extends Whitelist {

	public static Whitelist videos() {
		return basic().addTags("iframe")
				.addAttributes("iframe", "width", "height", "src", "frameborder", "allowfullscreen", "webkitAllowFullScreen", "mozallofullscreen")
				.addProtocols("iframe", "src", "http", "https");
	}
	
	public static Whitelist simpleText() {
		return Whitelist.simpleText().addTags("br");
	}
	
	public static Whitelist relaxed() {
		return Whitelist.relaxed()
					.addTags("iframe")
					.addAttributes("iframe", "src", "width", "height", "allowfullscreen")
					.addAttributes("p", "style")
					.addAttributes("img", "style")
					.addAttributes("span", "style")
					.addAttributes("div", "style");
	}
}
