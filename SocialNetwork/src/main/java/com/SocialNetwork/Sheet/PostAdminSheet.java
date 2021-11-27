package com.SocialNetwork.Sheet;

import java.sql.Date;
import java.sql.Time;

import com.SocialNetwork.Entity.Posts;

public class PostAdminSheet {
	public int postId;
	public String user;
	public String image;
	public Date date;
	public Time time;
	public int numLike;
	public int numComment;
	
	public PostAdminSheet(Posts post,int numLike,int numComment) {
		this.postId = post.getPost_id();
		this.user = post.getUserPost().getEmail();
		this.image = post.getImage();
		this.date = post.getDate();
		this.time = post.getTime();
		this.numLike = numLike;
		this.numComment = numComment;
	}
}
