package com.haraif.real_time_chat_application.interceptor;

import java.net.URI;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		URI uri = request.getURI();
		// expect ?username=alice
		String username = null;
		String query = uri.getQuery();
		if (query != null) {
			for (String param : query.split("&")) {
				if (param.startsWith("username=")) {
					username = param.split("=")[1];
				}
			}

		}
		if (username == null || username.isEmpty()) {
			return false;
		}
		attributes.put("username", username);
		attributes.put("principal", new StompPrincipal(username));
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
	}

}
