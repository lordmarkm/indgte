package com.baldwin.indgte.persistence.constants;

public enum Background {
	grass("Grass", "grass"),
	water("Water", "water"),
	waves("Waves", "waves"),
	whitesand("White Sands", "whitesand"),
	sillimanportal("Silliman Portal", "sillimanportal");
	
	private String name;
	private String filename;
	
	private Background(String name, String filename) {
		this.name = name;
		this.filename = filename;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFilename() {
		return filename;
	}
}
