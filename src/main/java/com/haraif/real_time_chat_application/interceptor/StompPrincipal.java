package com.haraif.real_time_chat_application.interceptor;

import java.security.Principal;

public class StompPrincipal implements Principal {

  private final String name;

  public StompPrincipal(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

}
