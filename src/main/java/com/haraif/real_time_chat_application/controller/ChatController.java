package com.haraif.real_time_chat_application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.haraif.real_time_chat_application.dto.ChatMessageDTO;
import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.service.ChatService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

	@Autowired
	private ChatService chatService;

	// client sends to /app/chat.send.{roomId}
	@MessageMapping("/chat.send.{roomId}")
	public void sendRoom(@DestinationVariable String roomId, @Valid ChatMessageDTO dto) {
		chatService.handleRoomMessage(dto, roomId);
	}

	// client sends to /app/chat.private
	@MessageMapping("/chat.private")
	public void sendPrivate(@Valid ChatMessageDTO dto) {
		chatService.handlePrivateMessage(dto);
	}

	// REST endpoint to fetch room history (so frontend can load history on join)
	@GetMapping(path = "/api/rooms/{roomId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ChatMessage> getRoomHistory(@PathVariable("roomId") String roomId) {
		return chatService.getRoomHistory(roomId);
	}

	// REST endpoint to fetch private message history (so frontend can load history
	// on join)
	@GetMapping(path = "/api/users/{sender}/{receiver}/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ChatMessage> getPrivateMessage(@PathVariable String sender, @PathVariable String receiver) {
		return chatService.getPrivateChatHistory(sender, receiver);
	}
}
