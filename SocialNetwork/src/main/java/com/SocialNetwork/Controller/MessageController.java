package com.SocialNetwork.Controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.GroupChat;
import com.SocialNetwork.Entity.JsonParseMessage;
import com.SocialNetwork.Entity.Message;
import com.SocialNetwork.Entity.User;
import com.SocialNetwork.Repository.GroupRepository;
import com.SocialNetwork.Repository.MessageRepository;
import com.SocialNetwork.Repository.UserRepository;
import com.SocialNetwork.Service.CalculateTime;
import com.SocialNetwork.Sheet.GroupSheet;
import com.SocialNetwork.Sheet.MessageSheet;
import com.alibaba.fastjson.JSON;

@Controller
public class MessageController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private MessageRepository messRepository;

	@GetMapping("/user/messagePage")
	public ModelAndView messagePage(Authentication authentication) {
		CalculateTime calTime = new CalculateTime();
		ModelAndView model = new ModelAndView("users/mess");
		User auth = userRepository.findByEmail(authentication.getName());
		List<GroupChat> groupChat = new ArrayList<GroupChat>();
		groupChat.addAll(auth.getReceiveMessage());
		groupChat.addAll(auth.getSendMessage());
		List<GroupSheet> groupSheets = new ArrayList<>();
		for (GroupChat g : groupChat) {
			List<Message> messages = g.getMessages();
			List<MessageSheet> messageSheets = new ArrayList<>();
			MessageSheet lastMessage = new MessageSheet();
			for (Message m : messages) {
				String userSend[] = m.getFromto().split(" ");
				Integer userSendId = Integer.valueOf(userSend[0]);
				MessageSheet messageSheet = new MessageSheet(m.getMessage_id(), m.getMessage(), m.isHasSeen(),
						m.isHasLiked(), userSendId == auth.getUser_id() ? true : false,
						calTime.calculateTime(m.getDate(), m.getTime()));
				lastMessage = messageSheet;
				messageSheets.add(messageSheet);
			}
			GroupSheet groupSheet = new GroupSheet(g.getGroup_id(),
					g.getReceiver().getUser_id() == auth.getUser_id() ? g.getReceiver() : g.getSender(), messageSheets,
					g.getReceiver().getUser_id() == auth.getUser_id() ? g.getSender() : g.getReceiver(),
					lastMessage.message, lastMessage.timeText);
			groupSheets.add(groupSheet);
		}
		model.addObject("groups", groupSheets);
		model.addObject("currentUser", auth);
		return model;
	}
	
	@PostMapping("/user/saveMessage")
	public @ResponseBody JsonParseMessage saveMessage(@RequestBody String jsonMessage) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage,JsonParseMessage.class);
		GroupChat groupChat = groupRepository.getById(Integer.valueOf(jsonParseMessage.groupId));
		//User sender = userRepository.findById(Integer.valueOf(jsonParseMessage.userId)).get();
		//User receiver = userRepository.findById(Integer.valueOf(jsonParseMessage.withUserId)).get();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		message.setDate(date);
		message.setFromto(jsonParseMessage.userId+" to "+jsonParseMessage.withUserId);
		message.setGroup(groupChat);
		message.setHasLiked(false);
		message.setHasSeen(false);
		message.setMessage(jsonParseMessage.message);
		message.setTime(time);
		messages.add(message);
		groupChat.setMessages(messages);
		Integer messageId = messRepository.save(message).getMessage_id();
		groupRepository.saveAndFlush(groupChat);
		jsonParseMessage.messageId = messageId.toString();
		return jsonParseMessage;
	}
	
	@PostMapping("/user/likeMessage")
	public @ResponseBody JsonParseMessage likeAndUnlike(@RequestBody String jsonMessage) {
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage,JsonParseMessage.class);
		Message message = messRepository.getById(Integer.valueOf(jsonParseMessage.messageId));
		if(jsonParseMessage.type.equals("like"))message.setHasLiked(true);
		else message.setHasLiked(false);
		messRepository.save(message);
		return jsonParseMessage;
	}
	
	@PostMapping("/user/deleteMessage")
	public @ResponseBody JsonParseMessage deleteMessage(@RequestBody String jsonMessage) {
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage,JsonParseMessage.class);
		Message message = messRepository.getById(Integer.valueOf(jsonParseMessage.messageId));
		messRepository.delete(message);
		return jsonParseMessage;
	}
}
