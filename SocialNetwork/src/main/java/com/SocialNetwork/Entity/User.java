package com.SocialNetwork.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users", schema = "socialnetwork")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer user_id;
	private String user_password;
	public String fullname;
	private String sex;
	private String email;
	private int locked;
	private int enabled;
	private String role;
	@Lob
	public String profile;
	@Lob
	private String cover;
	
	@OneToMany(mappedBy="userPost",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Posts> posts;
	
	@OneToMany(mappedBy="user2",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Friends> friend1;
	
	@OneToMany(mappedBy="user1",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Friends> friend2;
	
	@JsonIgnore
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Actions> actions;
	
	@OneToMany(mappedBy="sender",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Nontifications> nons1;
	
	public List<Nontifications> getNons1() {
		return nons1;
	}
	public void setNons1(List<Nontifications> nons1) {
		this.nons1 = nons1;
	}
	public List<Nontifications> getNons2() {
		return nons2;
	}
	public void setNons2(List<Nontifications> nons2) {
		this.nons2 = nons2;
	}
	@OneToMany(mappedBy="receiver",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Nontifications> nons2;
	
	public List<Actions> getLikes() {
		return actions;
	}
	public void setLikes(List<Actions> actions) {
		this.actions = actions;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public List<Posts> getPosts() {
		return posts;
	}
	public void setPosts(List<Posts> posts) {
		this.posts = posts;
	}
	public List<Friends> getFriends1() {
		return friend1;
	}
	public void setFriends1(List<Friends> friends1) {
		this.friend1 = friends1;
	}
	public List<Friends> getFriends2() {
		return friend2;
	}
	public void setFriends2(List<Friends> friend2) {
		this.friend2 = friend2;
	}
	
}
