package com.haraif.real_time_chat_application.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message")
@Data
@Builder
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String sender;
  private String receiver;

  @Size(max = 2000)
  private String content;

  private Instant timestamp;

  @Enumerated(EnumType.STRING)
  private MessageType type;

  private String roomId;

  public enum MessageType {
    CHAT, JOIN, LEAVE, PRIVATE
  }
}
