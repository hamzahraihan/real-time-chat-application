package com.haraif.real_time_chat_application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.haraif.real_time_chat_application.dto.ChatMessageDTO;
import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.repository.ChatMessageRepository;
import com.haraif.real_time_chat_application.service.ChatService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

	@Mock
	private SimpMessagingTemplate template;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@InjectMocks
	private ChatService chatService;

	@Test
	void testHandleRoomMessage() {
		ChatMessageDTO dto = new ChatMessageDTO();
		dto.setSender("alice");
		dto.setContent("hello from test");

		chatService.handleRoomMessage(dto, "room1");

		verify(chatMessageRepository).save(any(ChatMessage.class));
		verify(template).convertAndSend(eq("/topic/room.room1"), any(ChatMessage.class));

	}

	@Test
	void testHandlePrivateMessage() {
		ChatMessageDTO dto = new ChatMessageDTO();
		dto.setSender("alice");
		dto.setContent("hello to johnny");
		dto.setReceiver("johnny");

		chatService.handlePrivateMessage(dto);

		verify(chatMessageRepository).save(any(ChatMessage.class));
		verify(template).convertAndSendToUser(eq(dto.getReceiver()), eq("/queue/messages"), any(ChatMessage.class));
	}
}