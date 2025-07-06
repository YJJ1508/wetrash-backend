package yjj.wetrash.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.global.enums.ReportReason;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessageReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage reportedMessage;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member reporter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Builder
    public ChatMessageReport(ChatMessage chatMessage, Member reporter, ReportReason reason, LocalDateTime reportedAt){
        this.reportedMessage = chatMessage;
        this.reporter = reporter;
        this.reason = reason;
    }
}
