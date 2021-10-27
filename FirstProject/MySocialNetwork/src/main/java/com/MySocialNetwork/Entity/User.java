package com.MySocialNetwork.Entity;
import javax.persistence.Entity;
@Entity
public class User {
	private String fullName;
	private String email;
	private String gender;
	private String password;
	private String passwordconfirmation;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordconfirmation() {
		return passwordconfirmation;
	}
	public void setPasswordconfirmation(String passwordconfirmation) {
		this.passwordconfirmation = passwordconfirmation;
	}
	public User(String fullName, String email, String gender, String password, String passwordconfirmation) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.gender = gender;
		this.password = password;
		this.passwordconfirmation = passwordconfirmation;
	}
}
