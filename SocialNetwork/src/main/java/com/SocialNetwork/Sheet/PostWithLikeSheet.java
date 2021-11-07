package com.SocialNetwork.Sheet;

import java.util.List;

import com.SocialNetwork.Entity.Posts;

public class PostWithLikeSheet {
	public Posts Posts;
	public boolean isLiked;
	public int countLike;
	public int countComment;
	public List<CommentSheet> comments;
	public boolean isYourPost;
	public String time;
	public PostWithLikeSheet(Posts posts, boolean isLiked,int countLike,int countComment,List<CommentSheet> comments,boolean isYourPost,String time) {
		this.Posts = posts;
		this.isLiked = isLiked;
		this.countLike = countLike;
		this.countComment = countComment;
		this.comments = comments;
		this.isYourPost = isYourPost;
		this.time = time;
	}
}
