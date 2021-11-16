package com.SocialNetwork.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.SocialNetwork.Entity.JsonParseMessage;
import com.SocialNetwork.Repository.UserRepository;
import com.alibaba.fastjson.JSON;

@RestController
@Component
@ServerEndpoint(value="/user/messageServer/{userId}")
public class WebSocketServerEndPoint {
	private static Map<Integer, Session> onlineSessions = new ConcurrentHashMap<>();
	
	public void sendMessage(String message, Integer receiverId, String userId) {
		try {
			Session receiverSession = onlineSessions.get(receiverId);
			if (receiverSession!= null) receiverSession.getBasicRemote().sendText(message);
			onlineSessions.get(Integer.valueOf(userId)).getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void openSession(Session session,@PathParam("userId") String userId) {
		session.setMaxTextMessageBufferSize(1024*1024*64);
		onlineSessions.put(Integer.valueOf(userId), session);
	}
	
	@OnMessage
	public void onMessage(Session session,String jsonString) {
		JsonParseMessage message = JSON.parseObject(jsonString,JsonParseMessage.class);
		sendMessage(jsonString, Integer.valueOf(message.withUserId),message.userId);
	}
	
	@OnClose
	public void closeSession(Session session) {
		
	}
	
	/*@OnError
	public void errorSession(Session session,Throwable throwable) {
		System.out.println("Error");
	}*/
}
