package com.SocialNetwork.Sheet;

public class FriendSheet {
	private int id;
	private String fullname;
	private String image;
	public FriendSheet(int id, String fullname, String image) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.image = image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
