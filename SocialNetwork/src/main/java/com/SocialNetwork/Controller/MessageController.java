package com.SocialNetwork.Controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.SocialNetwork.Entity.Friends;
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
import com.SocialNetwork.Sheet.NewMessSheet;
import com.alibaba.fastjson.JSON;
import com.google.api.client.util.Lists;

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
		for (GroupChat g : com.google.common.collect.Lists.reverse(groupChat)) {
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
		List<Friends> friends = new ArrayList<>();
		friends.addAll(auth.getFriends1());
		friends.addAll(auth.getFriends2());
		List<NewMessSheet> newMessSheets = new ArrayList<>();
		for (Friends f : friends) {
			NewMessSheet newMessSheet = new NewMessSheet();
			if (f.getUser1().getUser_id() == auth.getUser_id()) {
				List<GroupChat> checkHasChatted = groupRepository.findHasChatted(auth.getUser_id(),
						f.getUser2().getUser_id());
				if (checkHasChatted.size() == 0) {
					newMessSheet = new NewMessSheet(f.getUser2().getUser_id(), f.getUser2().getFullname(),
							f.getUser2().getProfile());
					newMessSheets.add(newMessSheet);
				}
			} else {
				List<GroupChat> checkHasChatted = groupRepository.findHasChatted(auth.getUser_id(),
						f.getUser1().getUser_id());
				if (checkHasChatted.size() == 0) {
					newMessSheet = new NewMessSheet(f.getUser1().getUser_id(), f.getUser1().getFullname(),
							f.getUser1().getProfile());
					newMessSheets.add(newMessSheet);
				}
			}
				
		}
		model.addObject("friends",newMessSheets);
		model.addObject("groups", groupSheets);
		model.addObject("currentUser", auth);
		return model;
	}

	@PostMapping("/user/saveMessage")
	public @ResponseBody JsonParseMessage saveMessage(@RequestBody String jsonMessage) {
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(System.currentTimeMillis());
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage, JsonParseMessage.class);
		GroupChat groupChat = groupRepository.getById(Integer.valueOf(jsonParseMessage.groupId));
		// User sender =
		// userRepository.findById(Integer.valueOf(jsonParseMessage.userId)).get();
		// User receiver =
		// userRepository.findById(Integer.valueOf(jsonParseMessage.withUserId)).get();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		message.setDate(date);
		message.setFromto(jsonParseMessage.userId + " to " + jsonParseMessage.withUserId);
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
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage, JsonParseMessage.class);
		Message message = messRepository.getById(Integer.valueOf(jsonParseMessage.messageId));
		if (jsonParseMessage.type.equals("like"))
			message.setHasLiked(true);
		else
			message.setHasLiked(false);
		messRepository.save(message);
		return jsonParseMessage;
	}

	@PostMapping("/user/deleteMessage")
	public @ResponseBody JsonParseMessage deleteMessage(@RequestBody String jsonMessage) {
		JsonParseMessage jsonParseMessage = JSON.parseObject(jsonMessage, JsonParseMessage.class);
		Message message = messRepository.getById(Integer.valueOf(jsonParseMessage.messageId));
		messRepository.delete(message);
		return jsonParseMessage;
	}
	
	@PostMapping("/user/createGroup")
	public @ResponseBody GroupSheet createGroup(@RequestBody int receiverId,Authentication authentication) {
		GroupSheet groupSheet = new GroupSheet();
		User auth = userRepository.findByEmail(authentication.getName());
		User receiver = userRepository.findById(receiverId).get();
		if(groupRepository.findHasChatted(auth.getUser_id(), receiver.getUser_id()).size() == 0) {
			GroupChat groupChat = new GroupChat();
			groupChat.setSender(auth);
			groupChat.setReceiver(receiver);
			groupChat = groupRepository.save(groupChat);
			groupSheet = new GroupSheet(groupChat.getGroup_id(), auth, null, receiver, "", "");
			return groupSheet;
		}
		else return null;
	}
}
