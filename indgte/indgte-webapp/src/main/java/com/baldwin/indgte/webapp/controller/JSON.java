package com.baldwin.indgte.webapp.controller;

import java.util.HashMap;

public class JSON extends HashMap<String, Object> {
	private static final long serialVersionUID = -8234082507884717784L;
	
	public static JSON ok() {
		return new JSON().status("200").message("OK");
	}
	
	public static JSON status404() {
		return new JSON().status("404").message("Not found");
	}

	public static JSON status500(Exception e) {
		return new JSON().status("500").message(e.getClass() + ": " + e.getMessage());
	}
	
	public JSON status(String status) {
		super.put("status", status);
		return this;
	}
	
	public JSON message(String message) {
		super.put("message", message);
		return this;
	}
	
	public JSON put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
