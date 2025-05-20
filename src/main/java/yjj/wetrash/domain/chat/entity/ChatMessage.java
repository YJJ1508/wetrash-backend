package yjj.wetrash.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pinId;

    @ManyToOne
    private Member sender;

    private String message;

    private LocalDateTime createdAt;

    private int reportCount;

    @Builder
    public ChatMessage(Long pinId, Member sender, String message, LocalDateTime createdAt, int reportCount){
        this.pinId = pinId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
        this.reportCount = reportCount;
    }

    public void increaseReportCount(){
        this.reportCount++;
    }


}
