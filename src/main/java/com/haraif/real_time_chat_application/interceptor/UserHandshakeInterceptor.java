package com.haraif.real_time_chat_application.interceptor;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

// Identify users for private messaging (handshake / principal)

@Component
@Slf4j
public class UserHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		URI uri = request.getURI();
		log.info("WebSocket handshake request from: {}", uri);

		// expect ?username=alice
		String username = null;
		String query = uri.getQuery();
		if (query != null) {
			log.debug("Query parameters: {}", query);
			for (String param : query.split("&")) {
				if (param.startsWith("username=")) {
					username = URLDecoder.decode(param.split("=")[1], StandardCharsets.UTF_8);
					break;
				}
			}
		}

		if (username == null || username.trim().isEmpty()) {
			log.warn("WebSocket connection rejected: no username provided in query parameters");
			// Allow connection even without username for now, but don't set principal
			return true;
		}

		log.info("WebSocket connection accepted for user: {}", username);
		attributes.put("username", username);
		attributes.put("principal", new StompPrincipal(username));
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		if (exception != null) {
			log.error("WebSocket handshake failed", exception);
		} else {
			log.info("WebSocket handshake completed successfully");
		}
	}
}
