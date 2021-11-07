package com.SocialNetwork.Sheet;

import java.sql.Date;
import java.sql.Time;

import com.SocialNetwork.Entity.User;

public class CommentSheet {
	public int commentId;
	public String text;
	public User user;
	public Date date;
	public Time time;
	public CommentSheet(int commentId, String text, User user, Date date,Time time) {
		this.commentId = commentId;
		this.text = text;
		this.user = user;
		this.date = date;
		this.time = time;
	}
	public CommentSheet() {
		
	}
}
