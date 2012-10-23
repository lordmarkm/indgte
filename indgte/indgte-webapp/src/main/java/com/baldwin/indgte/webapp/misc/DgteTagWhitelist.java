package com.baldwin.indgte.webapp.misc;

import org.jsoup.safety.Whitelist;

public class DgteTagWhitelist extends Whitelist {

	public static Whitelist videos() {
		return basic().addTags("iframe")
				.addAttributes("iframe", "width", "height", "src", "frameborder", "allowfullscreen", "webkitAllowFullScreen", "mozallofullscreen")
				.addProtocols("iframe", "src", "http", "https");
	}
}
