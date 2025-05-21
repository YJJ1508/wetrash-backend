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
import yjj.wetrash.global.enums.ReportReason;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageAdminDTO {
    private Long chatMessageId;
    private Long pinId;
    private String writerNickname;
    private String writerEmail;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime messageCreatedAt;
    private int reportCount;

    private List<ChatMessageReportDetailDTO> reports;

    public static ChatMessageAdminDTO from(ChatMessage chatMessage, List<ChatMessageReport> chatMessageReport){

        List<ChatMessageReportDetailDTO> reportDetailDTO = chatMessageReport.stream()
                .map(ChatMessageReportDetailDTO::from)
                .collect(Collectors.toList());

        return ChatMessageAdminDTO.builder()
                .chatMessageId(chatMessage.getId())
                .pinId(chatMessage.getPinId())
                .writerEmail(chatMessage.getSender().getEmail())
                .writerNickname(chatMessage.getSender().getNickname())
                .message(chatMessage.getMessage())
                .messageCreatedAt(chatMessage.getCreatedAt())
                .reportCount(chatMessage.getReportCount())
                .reports(reportDetailDTO)
                .build();
    }
}
