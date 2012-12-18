package com.baldwin.indgte.webapp.misc;

import java.util.Locale;

public enum Language {
	cebuano("ceb_PH", "Binisaya"),
	english("en", "English"),
	chinese("zh_CN", "中文");
	
	private String locale;
	private String label;
	
	private Language(String locale, String label) {
		this.locale = locale;
		this.label = label;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return this.name();
	}
	
	public static Locale determineLocale(String localeStr) {
		Locale locale = null;
		switch(localeStr) {
			case "ceb_PH": locale = new Locale("ceb", "PH"); break;
			case "zh_CN": locale = new Locale("zh", "CN"); break;
			default: locale = new Locale("en");
		}
		return locale;
	}
}
