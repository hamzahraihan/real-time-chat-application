package com.haraif.real_time_chat_application.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

  @Override
  protected Principal determineUser(ServerHttpRequest request,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes) {
    return (Principal) attributes.get("principal");
  }
}
