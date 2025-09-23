package com.haraif.real_time_chat_application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.haraif.real_time_chat_application.dto.ChatMessageDTO;
import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.repository.ChatMessageRepository;
import com.haraif.real_time_chat_application.repository.ChatRoomRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	public ChatMessage handleRoomMessage(ChatMessageDTO dto, String roomId) {
		ChatMessage msg = ChatMessage.builder()
				.sender(dto.getSender())
				.content(dto.getContent())
				.timestamp(Instant.now())
				.type(ChatMessage.MessageType.CHAT)
				.roomId(roomId)
				.build();

		chatMessageRepository.save(msg);
		messagingTemplate.convertAndSend("/topic/room" + roomId, msg);
		log.info("[Room {}] {}: {}", roomId, msg.getSender(), msg.getContent());
		return msg;
	}

	public ChatMessage handlePrivateMessage(ChatMessageDTO dto) {
		ChatMessage msg = ChatMessage.builder()
				.sender(dto.getSender())
				.content(dto.getContent())
				.timestamp(Instant.now())
				.type(ChatMessage.MessageType.PRIVATE)
				.build();

		chatMessageRepository.save(msg);
		messagingTemplate.convertAndSendToUser(dto.getReceiver(), "/queue/messages", msg);
		log.info("[private {} -> {}] {}: {}", msg.getSender(), msg.getReceiver(), msg.getContent());
		return msg;
	}

	// helper: fetch recent message for a room
	public List<ChatMessage> getRoomHistory(String roomId) {
		return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
	}
}
