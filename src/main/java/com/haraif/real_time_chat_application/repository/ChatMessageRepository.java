package com.haraif.real_time_chat_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haraif.real_time_chat_application.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
  List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

  List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(String sender, String receiver);

}
