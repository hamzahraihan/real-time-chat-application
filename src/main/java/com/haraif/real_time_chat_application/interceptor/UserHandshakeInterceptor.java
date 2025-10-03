package com.haraif.real_time_chat_application.interceptor;

import java.net.URI;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

// Identify users for private messaging (handshake / principal)

@Component
@Slf4j
public class UserHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) {
		URI uri = request.getURI();
		MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
		String username = params.getFirst("username");

		if (username == null || username.trim().isEmpty()) {
			log.warn("Rejecting WebSocket connection: no username provided");
			return false;
		}

		attributes.put("principal", new StompPrincipal(username));
		log.info("Handshake accepted for user {}", username);
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Exception ex) {
		if (ex != null) {
			log.error("Handshake failed", ex);
		} else {
			log.info("Handshake success");
		}
	}
}
