package com.haraif.real_time_chat_application.controller;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventListener {

	@Autowired
	private SimpMessagingTemplate template;

	private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

	@EventListener
	public void handleSessionConnected(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = sha.getUser();
		if (user != null) {
			String username = user.getName();
			if (onlineUsers.add(username)) {
				log.info("User {} connected", username);
				broadcastPresence();
			}
		}
	}

	@EventListener
	public void handleSessionDisconnected(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = sha.getUser();
		if (user != null) {
			String username = user.getName();
			if (onlineUsers.remove(username)) {
				log.info("User {} disconnected", username);
				broadcastPresence();
			}
		}
	}

	private void broadcastPresence() {
		template.convertAndSend("/topic/presence", onlineUsers);
	}

	// Public methods to check online status
	public boolean isUserOnline(String username) {
		return onlineUsers.contains(username);
	}

	public Set<String> getOnlineUsers() {
		return Set.copyOf(onlineUsers); // Return immutable copy for safety
	}

	public int getOnlineUserCount() {
		return onlineUsers.size();
	}
}
