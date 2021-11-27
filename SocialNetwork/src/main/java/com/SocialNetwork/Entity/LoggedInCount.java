package com.SocialNetwork.Entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.SocialNetwork.Sheet.CountLogInSheet;
import com.SocialNetwork.Sheet.CountPostSheet;

@Entity
@Table(name="logged_in")
@NamedNativeQuery(name="LoggedInCount.countLogByDate",query="select l.date as date,count(l.date) as number from logged_in l group by l.date order by l.date asc",resultSetMapping="Mapping.CountLogSheet")
@SqlResultSetMapping(name="Mapping.CountLogSheet",classes=@ConstructorResult(columns= {@ColumnResult(name="date"),@ColumnResult(name="number",type=Integer.class)},targetClass=CountLogInSheet.class))
public class LoggedInCount {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer log_id;
	
	private String Email;
	private Date date;
	private Time time;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getLog_id() {
		return log_id;
	}

	public void setLog_id(Integer log_id) {
		this.log_id = log_id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
