package com.SocialNetwork.Entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="actions",schema="socialnetwork")
public class Actions {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int action_id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName="user_id")
	@JsonBackReference
	public User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id",referencedColumnName="post_id")
	@JsonBackReference
	public Posts post;
	public String type;
	public Date date;
	public Time time;
	public String text;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getAction_id() {
		return action_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Posts getPost() {
		return post;
	}

	public void setPost(Posts post) {
		this.post = post;
	}
	
	
}
