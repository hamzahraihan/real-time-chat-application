package com.haraif.real_time_chat_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haraif.real_time_chat_application.model.ChatMessage;
import com.haraif.real_time_chat_application.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
  List<ChatMessage> findRoomIdOrderByTimestampAsc(String roomId);

  List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(String sender, String receiver);
}
