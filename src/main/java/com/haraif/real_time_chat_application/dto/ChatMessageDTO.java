package com.haraif.real_time_chat_application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haraif.real_time_chat_application.model.ChatMessage.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {

	@JsonIgnore
	private String id;

	@NotBlank
	private String sender;

	private String receiver;

	@NotBlank
	private String content;

	private String roomId;

	@NotNull
	private MessageType type;

}
