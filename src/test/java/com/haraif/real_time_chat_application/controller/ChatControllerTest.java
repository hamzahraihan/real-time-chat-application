package com.haraif.real_time_chat_application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haraif.real_time_chat_application.dto.ChatMessageDTO;
import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.repository.ChatMessageRepository;
import com.haraif.real_time_chat_application.service.ChatService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ChatService chatService;

	@BeforeEach
	void setUp() {
		chatMessageRepository.deleteAll();
	}

	@Test
	void getPrivateMessageSuccess() throws Exception {
		// First, save a message to the database
		ChatMessageDTO dto = new ChatMessageDTO();
		dto.setSender("alice");
		dto.setReceiver("john");
		dto.setContent("hello john -from alice");

		// Save the message using the service
		chatService.handlePrivateMessage(dto);

		// Then test the API endpoint
		mockMvc.perform(
				get("/api/users/alice/john/history")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpectAll(status().isOk())
				.andDo(result -> {
					// The controller returns List<ChatMessage> directly
					List<ChatMessage> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<List<ChatMessage>>() {
							});
					assertNotNull(response);
					assertEquals("alice", response.get(0).getSender());
					assertEquals("john", response.get(0).getReceiver());
					assertEquals("hello john -from alice", response.get(0).getContent());
				});
	}

	@Test
	void getRoomHistorySuccess() throws Exception {
		mockMvc.perform(
				get("/api/rooms/room1/history")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpectAll(status().isOk())
				.andDo(result -> {
					// The controller returns List<ChatMessage> directly
					List<ChatMessage> response = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<List<ChatMessage>>() {
							});
					assertNotNull(response);
					// Response should be a valid list (empty for new room since DB is cleared)
				});
	}

	@Test
	void testHandleRoomMessage() {
		ChatMessageDTO dto = new ChatMessageDTO();
		dto.setSender("alice");
		dto.setContent("hello from test");

		chatService.handleRoomMessage(dto, "room1");

		// Verify message was saved to database
		List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByTimestampAsc("room1");
		assertNotNull(messages);
		assertEquals(1, messages.size());
		assertEquals("alice", messages.get(0).getSender());
		assertEquals("hello from test", messages.get(0).getContent());
	}

	@Test
	void testHandlePrivateMessage() {
		ChatMessageDTO dto = new ChatMessageDTO();
		dto.setSender("alice");
		dto.setContent("hello to johnny");
		dto.setReceiver("johnny");

		chatService.handlePrivateMessage(dto);

		// Verify message was saved to database
		List<ChatMessage> messages = chatMessageRepository.findBySenderAndReceiverOrderByTimestampAsc("alice", "johnny");
		assertNotNull(messages);
		assertEquals("alice", messages.get(0).getSender());
		assertEquals("johnny", messages.get(0).getReceiver());
		assertEquals("hello to johnny", messages.get(0).getContent());
	}

	@Test
	void testBidirectionalChatHistory() throws Exception {
		// Send message from alice to johnny
		ChatMessageDTO dto1 = new ChatMessageDTO();
		dto1.setSender("alice");
		dto1.setContent("hello johnny");
		dto1.setReceiver("johnny");
		chatService.handlePrivateMessage(dto1);

		// Send message from johnny to alice
		ChatMessageDTO dto2 = new ChatMessageDTO();
		dto2.setSender("johnny");
		dto2.setContent("hello alice");
		dto2.setReceiver("alice");
		chatService.handlePrivateMessage(dto2);

		// Test API call - should work both ways
		mockMvc.perform(
				get("/api/users/alice/johnny/history")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].sender").value("alice"))
				.andExpect(jsonPath("$[0].content").value("hello johnny"))
				.andExpect(jsonPath("$[1].sender").value("johnny"))
				.andExpect(jsonPath("$[1].content").value("hello alice"));

		// Test reverse API call - should return same conversation
		mockMvc.perform(
				get("/api/users/johnny/alice/history")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].sender").value("alice"))
				.andExpect(jsonPath("$[0].content").value("hello johnny"))
				.andExpect(jsonPath("$[1].sender").value("johnny"))
				.andExpect(jsonPath("$[1].content").value("hello alice"));
	}

	// @Test
	// void testHandleRoomMessage() {
	// ChatMessageDTO dto = new ChatMessageDTO();
	// dto.setSender("alice");
	// dto.setContent("hello from test");

	// chatService.handleRoomMessage(dto, "room1");

	// verify(chatMessageRepository).save(any(ChatMessage.class));
	// verify(template).convertAndSend(eq("/topic/room.room1"),
	// any(ChatMessage.class));
	// }

	// @Test
	// void testHandlePrivateMessage() {
	// ChatMessageDTO dto = new ChatMessageDTO();
	// dto.setSender("alice");
	// dto.setContent("hello to johnny");
	// dto.setReceiver("johnny");

	// chatService.handlePrivateMessage(dto);

	// verify(chatMessageRepository).save(any(ChatMessage.class));
	// verify(template).convertAndSendToUser(eq(dto.getReceiver()),
	// eq("/queue/messages"), any(ChatMessage.class));
	// }
}