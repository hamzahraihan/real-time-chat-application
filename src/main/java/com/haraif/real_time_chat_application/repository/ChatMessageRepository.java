package com.haraif.real_time_chat_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.haraif.real_time_chat_application.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
  List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

  List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(String sender, String receiver);

  // @Query("SELECT m FROM ChatMessage m WHERE " +
  // "(m.sender = :user1 AND m.receiver = :user2) OR " +
  // "(m.sender = :user2 AND m.receiver = :user1) " +
  // "ORDER BY m.timestamp ASC")
  @Query("""
          SELECT m FROM ChatMessage m WHERE
          (m.sender = :user1 AND m.receiver = :user2) OR
          (m.sender = :user2 AND m.receiver = :user1)
          ORDER BY m.timestamp ASC
      """)
  List<ChatMessage> findBidirectionalChatHistory(@Param("user1") String user1, @Param("user2") String user2);
}
