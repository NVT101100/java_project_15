package com.SocialNetwork.Controller;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.SocialNetwork.Entity.JsonParseMessage;
import com.alibaba.fastjson.JSON;

@RestController
@Component
@ServerEndpoint("/user/callServer/{userId}")
public class CallServerEndPoint {
	private static Map<Integer, Session> onlineSessions = new ConcurrentHashMap<>();
	@OnOpen
	public void openSession(Session session,@PathParam("userId") String userId) {
		session.setMaxTextMessageBufferSize(1024*1024*32);
		onlineSessions.put(Integer.valueOf(userId), session);
	}
	
	@OnMessage
	public void onMessage(Session session,String jsonString,@PathParam("userId") String userId) {
		System.out.println(jsonString);
		try {
			if(Integer.valueOf(userId) == 1) onlineSessions.get(6).getBasicRemote().sendText(jsonString);
			else onlineSessions.get(1).getBasicRemote().sendText(jsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnClose
	public void closeSession(Session session) {
		
	}
	
}
