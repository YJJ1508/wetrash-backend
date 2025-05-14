package yjj.wetrash.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pinId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(Long pinId, String sender, String message, LocalDateTime createdAt){
        this.pinId = pinId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }
}
