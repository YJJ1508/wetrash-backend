package yjj.wetrash.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageAdminDTO {
    private Long chatMessageId;
    private Long pinId;
    private Member sender;
    private String message;
    private List<ChatMessageReport> reportedMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime messageCreatedAt;
    private int reportCount;

    public static ChatMessageAdminDTO from(ChatMessage chatMessage, List<ChatMessageReport> chatMessageReport){

        return ChatMessageAdminDTO.builder()
                .chatMessageId(chatMessage.getId())
                .pinId(chatMessage.getPinId())
                .sender(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .reportedMessage(chatMessageReport)
                .messageCreatedAt(chatMessage.getCreatedAt())
                .reportCount(chatMessage.getReportCount())
                .build();
    }
}
