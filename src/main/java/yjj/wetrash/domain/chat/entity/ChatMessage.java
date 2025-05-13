package yjj.wetrash.domain.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id @GeneratedValue
    private Long id;

    private Long pinId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
}
