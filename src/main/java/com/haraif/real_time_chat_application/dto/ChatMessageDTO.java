package com.haraif.real_time_chat_application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {

  @NotBlank
  private String sender;

  private String receiver;

  @NotBlank
  private String content;

  private String roomId;

}
