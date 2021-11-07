package com.SocialNetwork.Entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="posts",schema = "socialnetwork")
public class Posts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public int post_id;
	@Lob
	private String image;
	private String status;
	private Date date;
	private Time time;
	public User getUserPost() {
		return userPost;
	}
	public void setUserPost(User userPost) {
		this.userPost = userPost;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",referencedColumnName="user_id")
	@JsonBackReference
	public User userPost;
	
	@OneToMany(mappedBy="post",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Actions> actions;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="post",cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Nontifications> nontifications;
	
	public List<Nontifications> getNontifications() {
		return nontifications;
	}
	public void setNontifications(List<Nontifications> nontifications) {
		this.nontifications = nontifications;
	}
	public List<Actions> getActions() {
		return actions;
	}
	public void setAction(List<Actions> actions) {
		this.actions = actions;
	}
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
