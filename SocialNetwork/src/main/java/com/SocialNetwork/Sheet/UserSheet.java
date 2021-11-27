package com.SocialNetwork.Sheet;

import com.SocialNetwork.Entity.User;

public class UserSheet {
	public Integer Id;
	public String email;
	public String fullname;
	public String profile;
	public String cover;
	public Integer status;
	public Integer postNumber;
	public Integer messageNumber;
	
	public UserSheet (User user,int postNum,int messNum) {
		this.Id = user.getUser_id();
		this.email = user.getEmail();
		this.fullname = user.getFullname();
		this.profile = user.getProfile();
		this.cover = user.getCover();
		this.status = user.getEnabled();
		this.postNumber = postNum;
		this.messageNumber = messNum;
	}
}
