package com.baldwin.indgte.persistence.dto;

public class Fame {
	
	static enum Title {
		unrecognized("", 0),
		unremarkable("", 160),
		recognized("Recognized", 320),
		tolerated("Known", 434),
		acknowledged("Acknowledged", 551),
		respected("Respected", 677),
		wellknown("Well Known", 806),
		liked("Liked", 943),
		memorable("Memorable", 1084),
		localhero("Local Hero", 1231),
		locallegend("Local Legend", 1378),
		honored("Honored", 1532),
		prominent("Prominent", 1689),
		widelyknown("Widely Known", 1844),
		remarkable("Remarkable", 2010),
		folkhero("Folk Hero", 2172),
		ascendant("Ascendant", 2340),
		distinguished("Distinguished", 2508),
		prestigious("Prestigious", 2675),
		eminent("Eminent", 2853),
		famous("Famous", 3024),
		acclaimed("Acclaimed", 3199),
		illustrious("Illustrious", 3380),
		renowned("Renowned", 3556),
		celebrated("Celebrated", 3737),
		lionized("Lionized", 3923),
		glorious("Glorious", 4104),
		legendary("Legendary", 4289),
		exalted("Exalted", 4478),
		fabled("Fabled", 4661),
		mythic("Mythic", 4848),
		immortal("Immortal", 5038),
		demigod("Demigod", 5232),
		god("God", 9999);
		
		private int minExperience = 0;
		private String title;
		
		private Title(String title, int minExperience) {
			this.minExperience = minExperience;
			this.title = title;
		}

		public int getMinExperience() {
			return minExperience;
		}

		public String getTitle() {
			return title;
		}
	}
	
	private int total = 0;
	private int reviewfame = 0;
	private int postfame = 0;
	private int entityfame = 0;
	private int friendshipfame = 0;
	
	public static String getTitle(int total) {
		for(Title title : Title.values()) {
			if(total < title.getMinExperience()) {
				int ordinal = title.ordinal();
				
				if(ordinal > 0) {
					return Title.values()[ordinal - 1].getTitle();
				} else {
					break;
				}
			}
		}
		
		return "";
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getReviewfame() {
		return reviewfame;
	}
	public void setReviewfame(int reviewfame) {
		this.reviewfame = reviewfame;
	}
	public int getPostfame() {
		return postfame;
	}
	public void setPostfame(int postfame) {
		this.postfame = postfame;
	}
	public int getEntityfame() {
		return entityfame;
	}
	public void setEntityfame(int entityfame) {
		this.entityfame = entityfame;
	}
	public int getFriendshipfame() {
		return friendshipfame;
	}
	public void setFriendshipfame(int friendshipfame) {
		this.friendshipfame = friendshipfame;
	}
	
}