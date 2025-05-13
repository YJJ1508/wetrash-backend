package yjj.wetrash.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import yjj.wetrash.domain.chat.entity.MessageType;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private MessageType type; //ENTER, TALK
    private Long pinId;
    private String sender;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
}
