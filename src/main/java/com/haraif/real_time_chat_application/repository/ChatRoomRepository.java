package com.haraif.real_time_chat_application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haraif.real_time_chat_application.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
  Optional<ChatRoom> findByName(String name);
}
