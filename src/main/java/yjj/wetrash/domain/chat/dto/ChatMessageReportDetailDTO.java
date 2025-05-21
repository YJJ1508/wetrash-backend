package yjj.wetrash.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.global.enums.ReportReason;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageReportDetailDTO {
    private String reporterNickname;
    private String reporterEmail;
    private ReportReason reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportedAt;

    public static ChatMessageReportDetailDTO from(ChatMessageReport report){
        return ChatMessageReportDetailDTO.builder()
                .reporterEmail(report.getReporter().getEmail())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason())
                .reportedAt(report.getReportedAt())
                .build();
    }
}
