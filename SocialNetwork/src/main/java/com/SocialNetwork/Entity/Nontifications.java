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
@Table(name = "nontifications", schema = "socialnetwork")
public class Nontifications {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public int non_id;

	private String type;
	private String text;
	private boolean hasSeen;
	private Date date;
	private Time time;
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

	public boolean isHasSeen() {
		return hasSeen;
	}

	public void setHasSeen(boolean hasSeen) {
		this.hasSeen = hasSeen;
	}

	public int getNon_id() {
		return non_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
	@JsonBackReference
	public User receiver;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "user_id")
	@JsonBackReference
	public User sender;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id",referencedColumnName="post_id")
	@JsonBackReference
	public Posts post;

	public Posts getPost() {
		return post;
	}

	public void setPost(Posts post) {
		this.post = post;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
}
