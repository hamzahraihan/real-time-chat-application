package com.haraif.real_time_chat_application.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haraif.real_time_chat_application.controller.WebSocketEventListener;
import com.haraif.real_time_chat_application.dto.ChatMessageDTO;
import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.repository.ChatMessageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private WebSocketEventListener onlineUsersService;

	// @Autowired
	// private ChatRoomRepository chatRoomRepository;

	@Transactional
	public ChatMessage handleRoomMessage(ChatMessageDTO dto, String roomId) {
		ChatMessage msg = ChatMessage.builder()
				.id(UUID.randomUUID().toString())
				.sender(dto.getSender())
				.content(dto.getContent())
				.timestamp(Instant.now())
				.type(dto.getType())
				.roomId(roomId)
				.build();

		chatMessageRepository.save(msg);
		messagingTemplate.convertAndSend("/topic/room." + roomId, msg);
		log.info("[Room {}] {}: {}", roomId, msg.getSender(), msg.getContent());
		return msg;
	}

	@Transactional
	public ChatMessage handlePrivateMessage(ChatMessageDTO dto) {
		ChatMessage msg = ChatMessage.builder()
				.id(UUID.randomUUID().toString())
				.sender(dto.getSender())
				.content(dto.getContent())
				.receiver(dto.getReceiver())
				.timestamp(Instant.now())
				.type(dto.getType())
				.build();

		chatMessageRepository.save(msg);

		// Send to receiver
		log.info("Sending private message to receiver: {} on topic: /user/{}/queue/private", dto.getReceiver(),
				dto.getReceiver());
		messagingTemplate.convertAndSendToUser(msg.getReceiver(), "/queue/private", msg);

		// sender also receiver it is own message
		log.info("Sending private message to sender: {} on topic: /user/{}/queue/private", dto.getSender(),
				dto.getSender());
		messagingTemplate.convertAndSendToUser(msg.getSender(), "/queue/private", msg);

		log.info("[private] {} -> {}: {}", msg.getSender(), msg.getReceiver(), msg.getContent());
		return msg;
	}

	// helper: fetch recent message for a room
	@Transactional(readOnly = true)
	public List<ChatMessage> getRoomHistory(String roomId) {
		List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);

		return chatMessages;
	}

	@Transactional(readOnly = true)
	public List<ChatMessage> getPrivateChatHistory(String sender, String receiver) {
		// Fetch messages in both directions between the two users
		List<ChatMessage> chatMessages = chatMessageRepository.findBidirectionalChatHistory(sender, receiver);

		return chatMessages;
	}

	// Online presence tracking methods
	public boolean isUserOnline(String username) {
		// This will be injected from WebSocketEventListener
		return onlineUsersService.isUserOnline(username);
	}

	public Set<String> getOnlineUsers() {
		// This will be injected from WebSocketEventListener
		return onlineUsersService.getOnlineUsers();
	}
}
