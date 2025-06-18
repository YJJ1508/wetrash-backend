package yjj.wetrash.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.chat.dto.ChatMessageDTO;
import yjj.wetrash.domain.chat.service.ChatService;
import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.exception.ErrorResponse;
import yjj.wetrash.global.security.CustomDetails;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    //클라이언트가 websocket 으로 보낸 메세지 처리 (enter or send)
    @MessageMapping("/send")
    public void handleMessage(@RequestBody ChatMessageDTO chatMessageDTO, Principal principal){
        chatService.processMessage(chatMessageDTO, principal);
    }

    //최근 30분 메세지 불러오기 (채팅방 입장할 때)
    @GetMapping("/messages/{pinId}")
    public ResponseEntity<List<ChatMessageDTO>> getRecentMessages(@PathVariable Long pinId){
        List<ChatMessageDTO> messages = chatService.getRecentMessages(pinId);
        return ResponseEntity.ok(messages);
    }

    //정지 회원 채팅 메세지 제한하기
    @MessageExceptionHandler(CustomException.class)
    public void handleException(CustomException ex, Principal principal){
        log.info("예외 핸들러 동작");
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                new ErrorResponse(403, ex.getMessage())
        );
    }

}
