package com.SocialNetwork.Sheet;

import java.util.List;

import com.SocialNetwork.Entity.GroupChat;
import com.SocialNetwork.Entity.User;

public class GroupSheet {
	public int groupId;
	public User user;
	public List<MessageSheet> message;
	public User withUser;
	public String lastMessage;
	public String timeText;
	public GroupSheet(int groupId, User user, List<MessageSheet> message, User withUser, String lastMessage,
			String timeText) {
		this.groupId = groupId;
		this.user = user;
		this.message = message;
		this.withUser = withUser;
		this.lastMessage = lastMessage;
		this.timeText = timeText;
	}
	
}