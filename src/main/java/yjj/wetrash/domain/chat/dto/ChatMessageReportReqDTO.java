package yjj.wetrash.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.global.enums.ReportReason;

@Getter
@NoArgsConstructor
public class ChatMessageReportReqDTO {
    private Long chatMessageId;
    private ReportReason reason;

    public ChatMessageReportReqDTO(Long chatMessageId, ReportReason reason) {
        this.chatMessageId = chatMessageId;
        this.reason = reason;
    }
}
