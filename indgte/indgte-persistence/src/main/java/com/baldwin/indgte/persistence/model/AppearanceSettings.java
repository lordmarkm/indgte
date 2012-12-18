package com.baldwin.indgte.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import com.baldwin.indgte.persistence.constants.Background;
import com.baldwin.indgte.persistence.constants.Theme;

@Embeddable
public class AppearanceSettings {
	@Column
	private String locale = "en";
	
	@Enumerated
	@Column
	private Background background = Background.grass;
	
	@Enumerated
	@Column
	private Theme theme = Theme.flick;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}
}
