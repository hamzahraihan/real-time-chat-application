package com.haraif.real_time_chat_application.interceptor;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// Identify users for private messaging (handshake / principal)

@Component
@Slf4j
public class UserHandshakeInterceptor implements HandshakeInterceptor {

	@Autowired
	private JwtDecoder jwtDecoder;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) {
		if (!(request instanceof ServletServerHttpRequest serverHttpRequest)) {
			return false;
		}

		HttpServletRequest httpServletRequest = serverHttpRequest.getServletRequest();
		String token = null;

		String authHeader = httpServletRequest.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // removing bearer
		}

		if (token == null) {
			token = httpServletRequest.getParameter("token");
		}

		if (token == null) {
			log.warn("Missing or invalid Authorization header");
			return false;
		}

		Jwt jwt = jwtDecoder.decode(token);
		String username = jwt.getSubject();
		Principal userPrincipal = new StompPrincipal(username);
		attributes.put("principal", userPrincipal);

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
