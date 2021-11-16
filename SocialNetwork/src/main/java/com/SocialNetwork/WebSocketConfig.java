package com.SocialNetwork;

import java.lang.annotation.Annotation;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableSpringConfigured
public class WebSocketConfig {
	@Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
	@Bean
	public ServerEndpoint serverEndpoint() {
		return new ServerEndpoint() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String value() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] subprotocols() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Class<? extends Encoder>[] encoders() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Class<? extends Decoder>[] decoders() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Class<? extends Configurator> configurator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}