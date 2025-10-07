package com.haraif.real_time_chat_application.dto;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PresenceMessageDTO {

  @NotNull
  private PresenceType type;

  private String user;

  private Set<String> users;

  public enum PresenceType {
    USER_ONLINE,
    USER_OFFLINE,
    ONLINE_USERS_LIST
  }

}
