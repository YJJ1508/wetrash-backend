package yjj.wetrash.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.chat.dto.ChatMessageDTO;
import yjj.wetrash.domain.chat.service.ChatService;
import yjj.wetrash.global.security.CustomDetails;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    //클라이언트가 websocket 으로 보낸 메세지 처리 (enter or send)
    @MessageMapping("/send")
    public void handleMessage(@RequestBody ChatMessageDTO chatMessageDTO){
        chatService.processMessage(chatMessageDTO);
    }

    //최근 30분 메세지 불러오기 (채팅방 입장할 때)
    @GetMapping("/messages/{pinId}")
    public ResponseEntity<List<ChatMessageDTO>> getRecentMessages(@PathVariable Long pinId){
        List<ChatMessageDTO> messages = chatService.getRecentMessages(pinId);
        return ResponseEntity.ok(messages);
    }
}
