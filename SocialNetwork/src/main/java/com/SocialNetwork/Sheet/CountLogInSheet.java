package com.SocialNetwork.Sheet;

import java.sql.Date;

public class CountLogInSheet {
	private java.util.Date date;
	private Integer number;
	public CountLogInSheet(java.util.Date date, Integer number) {
		this.date = date;
		this.number = number;
	}
	public java.util.Date getDate() {
		return date;
	}
	public Integer getNumber() {
		return number;
	}
	
}
