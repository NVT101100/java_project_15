package com.SocialNetwork.Entity;

import java.sql.Date;

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
@Table(name = "friends", schema="socialnetwork")
public class Friends {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int friend_id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user1_id",referencedColumnName="user_id")
	@JsonBackReference
	private User user1;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user2_id",referencedColumnName="user_id")
	@JsonBackReference
	private User user2;
	
	private boolean confirmed;
	
	private boolean blocked;
	private boolean waiting;
	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getFriend_id() {
		return friend_id;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}
	
}
