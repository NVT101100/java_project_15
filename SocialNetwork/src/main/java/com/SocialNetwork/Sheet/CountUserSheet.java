package com.SocialNetwork.Sheet;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class CountUserSheet {
	private java.util.Date date;
	private Integer number;
	public CountUserSheet(java.util.Date date,Integer number) {
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
