package com.haraif.real_time_chat_application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.haraif.real_time_chat_application.interceptor.UserHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.addInterceptors(new UserHandshakeInterceptor())
				.setAllowedOriginPatterns("http://localhost:5173", "http://localhost:3000")
				.withSockJS();

	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
		// registry.setUserDestinationPrefix("/user");
	}

	// @Bean
	// DefaultHandshakeHandler handshakeHandler() {
	// return new DefaultHandshakeHandler() {
	// @Override
	// protected Principal determineUser(ServerHttpRequest request, WebSocketHandler
	// wsHandler,
	// Map<String, Object> attributes) {
	// // For demo: use a query param ?username=alice
	// String username = request.getURI().getQuery().replace("username=", "");
	// return () -> username;
	// }
	// };
	// }

}
