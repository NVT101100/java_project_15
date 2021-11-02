package com.SocialNetwork.Entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="posts",schema = "socialnetwork")
public class Posts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int post_id;
	@Lob
	private String image;
	private String status;
	private String date;
	private String time;
	public User getUserPost() {
		return userPost;
	}
	public void setUserPost(User userPost) {
		this.userPost = userPost;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName="user_id")
	public User userPost;
	
	public int getPost_id() {
		return post_id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String Time) {
		time = Time;
	}
}
