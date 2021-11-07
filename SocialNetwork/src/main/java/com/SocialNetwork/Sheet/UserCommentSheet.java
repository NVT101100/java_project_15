package com.SocialNetwork.Sheet;

public class UserCommentSheet {
	public String fullname;
	public String profile;
	public Integer actionId;
	public String text;
	public UserCommentSheet(String fullname, String profile,Integer actionId, String text) {
		this.fullname = fullname;
		this.actionId = actionId;
		this.profile = profile;
		this.text = text;
	}
	
}
