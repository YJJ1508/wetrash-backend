package yjj.wetrash.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.global.enums.ReportReason;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatMessage reportedMessage;

    @ManyToOne
    private Member reporter;

    private ReportReason reason;

    @CreatedDate
    private LocalDateTime reportedAt;

    @Builder
    public ChatMessageReport(ChatMessage chatMessage, Member reporter, ReportReason reason){
        this.reportedMessage = chatMessage;
        this.reporter = reporter;
        this.reason = reason;
    }
}
