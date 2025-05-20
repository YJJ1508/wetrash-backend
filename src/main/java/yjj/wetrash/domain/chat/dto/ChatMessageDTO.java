package yjj.wetrash.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.MessageType;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id; // 신고 시 client 에서 객체 서버로 보내기 위함
    private MessageType type; //ENTER, TALK
    private Long pinId;
    private String sender;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    public ChatMessage toEntity(ChatMessageDTO dto, Member sender){
        return ChatMessage.builder()
                .pinId(dto.getPinId())
                .sender(sender)
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .reportCount(0)
                .build();
    }
}
