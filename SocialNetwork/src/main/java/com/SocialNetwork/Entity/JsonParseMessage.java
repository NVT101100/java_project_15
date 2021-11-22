package com.SocialNetwork.Entity;

public class JsonParseMessage {
	public String withUserId;
	public String message;
	public String userId;
	public String groupId;
	public String type;
	public String messageId;
	//public User user;
	//public String toUser;
	public JsonParseMessage(String withUserId, String message, String userId, String groupId,String type,String messageId) {
		this.withUserId = withUserId;
		this.message = message;
		this.userId = userId;
		this.groupId = groupId;
		this.type = type;
		this.messageId = messageId;
	}
	/*public JsonParseMessage(String message, String groupId, String type, String messageId, User user, String toUser) {
		this.message = message;
		this.groupId = groupId;
		this.type = type;
		this.messageId = messageId;
		this.user = user;
		this.toUser = toUser;
	}*/
}
