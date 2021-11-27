package com.SocialNetwork.Sheet;

import java.util.Date;

public class CountPostSheet {
	private Date date;
	private Integer number;
	public CountPostSheet(Date date, Integer number) {
		this.date = date;
		this.number = number;
	}
	public Date getDate() {
		return date;
	}
	public Integer getNumber() {
		return number;
	}
	
}
