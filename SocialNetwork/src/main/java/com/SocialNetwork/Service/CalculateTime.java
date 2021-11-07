package com.SocialNetwork.Service;

import java.sql.Date;
import java.sql.Time;

public class CalculateTime {
	private Date currentDate = new Date(System.currentTimeMillis());
	private Time currentTime = new Time(System.currentTimeMillis());
	public String calculateTime(Date date,Time time) {
		int year = currentDate.toLocalDate().getYear() - date.toLocalDate().getYear();
		int month = currentDate.toLocalDate().getMonthValue() - date.toLocalDate().getMonthValue();
		int day = currentDate.toLocalDate().getDayOfMonth() - date.toLocalDate().getDayOfMonth();
		int hour = currentTime.toLocalTime().getHour() - time.toLocalTime().getHour();
		int minute = currentTime.toLocalTime().getMinute() - time.toLocalTime().getMinute();
		if (year != 0) {
			return year +" năm trước";
		}
		else if (month != 0) {
			return month+" tháng trước";
		}
		else if(day != 0) {
			return day+" ngày trước";
		}
		else if(hour != 0) {
			return hour+" giờ trước";
		}
		else if(minute != 0) {
			return minute+" phút trước";
		}
		else return "Vừa xong";
	}
}
