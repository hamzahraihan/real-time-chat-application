package com.haraif.real_time_chat_application.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/users")
public class UserPresenceController {

  @Autowired
  private WebSocketEventListener webSocketEventListener;

  // REST endpoint to check if a user is online
  @GetMapping(path = "/{username}/online", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean isUserOnline(@PathVariable("username") String username) {
    return webSocketEventListener.isUserOnline(username);
  }

  // REST endpoint to get all online users
  @GetMapping(path = "/online", produces = MediaType.APPLICATION_JSON_VALUE)
  public Set<String> getOnlineUsers() {
    return webSocketEventListener.getOnlineUsers();
  }

}
