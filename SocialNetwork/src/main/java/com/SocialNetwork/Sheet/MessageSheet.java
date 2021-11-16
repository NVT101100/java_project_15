package com.SocialNetwork.Sheet;

public class MessageSheet {
	public Integer messageId;
	public String message;
	public boolean hasSeen;
	public boolean hasLike;
	public boolean isAuthSend;
	public String timeText;
	public MessageSheet(Integer messageId, String message, boolean hasSeen, boolean hasLike, boolean isAuthSend,
			String timeText) {
		this.messageId = messageId;
		this.message = message;
		this.hasSeen = hasSeen;
		this.hasLike = hasLike;
		this.isAuthSend = isAuthSend;
		this.timeText = timeText;
	}
	public MessageSheet() {
		
	}
}
