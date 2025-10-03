package com.haraif.real_time_chat_application.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.haraif.real_time_chat_application.interceptor.UserHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.addInterceptors(new UserHandshakeInterceptor())
				.setHandshakeHandler(handshakeHandler())
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
	}

	@Bean
	DefaultHandshakeHandler handshakeHandler() {
		return new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request,
					WebSocketHandler wsHandler,
					Map<String, Object> attributes) {
				return (Principal) attributes.get("principal"); // ✅ return your StompPrincipal
			}
		};
	}

}
