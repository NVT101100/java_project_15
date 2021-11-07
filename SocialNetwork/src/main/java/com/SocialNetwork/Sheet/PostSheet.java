package com.SocialNetwork.Sheet;

import java.sql.Date;
import java.sql.Time;

public class PostSheet {
	private byte[] image;
	private String status;
	private Date date;
	private Time time;
	private boolean isliked;
	public boolean isIsliked() {
		return isliked;
	}
	public void setIsliked(boolean isliked) {
		this.isliked = isliked;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
