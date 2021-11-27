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

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.SocialNetwork.Entity.JsonParseMessage;
import com.SocialNetwork.Repository.UserRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

@RestController
@Component
@ServerEndpoint(value="/user/WebSocket/{Page}/{userId}")
public class WebSocketServerEndPoint {
	private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
	
	public void sendMessage(String message,String userId, String toUser,String Page,String type) {
		try {
			if(Page.equals("homePage")){
				Session receiverSession = onlineSessions.get(Page+toUser);
				if (receiverSession!= null) receiverSession.getBasicRemote().sendText(message);
				if(!type.equals("acceptfriend")) if(!userId.equals(toUser))onlineSessions.get(Page+userId).getBasicRemote().sendText(message);
			}
			if(Page.equals("profilePage")) {
				if(type.equals("addfriend")) {
					Session receiverSession = onlineSessions.get("homePage"+toUser);
					if (receiverSession != null) receiverSession.getBasicRemote().sendText(message);
				}
			}
			if(Page.equals("messagePage")) {
				Session receiverSession = onlineSessions.get(Page+toUser);
				if(receiverSession != null) {
					if(!receiverSession.isOpen()) {
						receiverSession = onlineSessions.get("homePage"+toUser);
						if(receiverSession!=null) {
							receiverSession.getBasicRemote().sendText(message);
						}
					}
					else receiverSession.getBasicRemote().sendText(message);
				}
				else {
					receiverSession = onlineSessions.get("homePage"+toUser);
					if(receiverSession!=null) {
						receiverSession.getBasicRemote().sendText(message);
					}
				}
				Session userSession = onlineSessions.get(Page+userId);
				if(userSession!=null) userSession.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void openSession(Session session,@PathParam("userId") String userId,@PathParam("Page") String page) {
		session.setMaxTextMessageBufferSize(1024*1024*128);
		onlineSessions.put(page+userId, session);
	}
	
	@OnMessage
	public void onMessage(Session session,String jsonString,@PathParam("userId") String userId) {
		System.out.println(jsonString);
		JSONObject jsonObject = JSON.parseObject(jsonString);
		String toUser = jsonObject.getString("toUser");
		String Page = jsonObject.getString("page");
		String type = jsonObject.getJSONObject("content").getString("type");
		sendMessage(jsonString,userId,toUser,Page,type);
	}
	
	@OnClose
	public void closeSession(Session session,@PathParam("userId") String userId,@PathParam("Page") String page) {
		onlineSessions.remove(page+userId);
	}
	
	/*@OnError
	public void errorSession(Session session,Throwable throwable) {
		System.out.println("Error");
	}*/
}
