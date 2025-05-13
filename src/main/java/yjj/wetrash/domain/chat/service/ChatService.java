package yjj.wetrash.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.chat.dto.ChatMessageDTO;
import yjj.wetrash.domain.chat.entity.MessageType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processMessage(ChatMessageDTO messageDTO){
        //1.timestamp 설정
        messageDTO.setCreatedAt(LocalDateTime.now());
        //enter 일 경우 입장 메세지 셋
        if (messageDTO.getType() == MessageType.ENTER) {
            messageDTO.setMessage(messageDTO.getSender() + "님이 입장하셨습니다.");
        }
        //2.redis 저장 - 30분
        if (messageDTO.getType() == MessageType.TALK){
            log.info("talk 타입 출력중: '{}'", messageDTO.getMessage());
        }
        try{
            String key = "chat:pin:"+messageDTO.getPinId();
            String json = objectMapper.writeValueAsString(messageDTO);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.expire(key, Duration.ofMinutes(30)); //TTL 30분 설정
        } catch (JsonProcessingException e){
            log.error("채팅 메세지 json 변환 실패", e);
        }
        //3.핀 채팅방에 broadcast
        String destination = "/sub/pin/" + messageDTO.getPinId();
        messagingTemplate.convertAndSend(destination, messageDTO);
    }

    @Transactional
    public List<ChatMessageDTO> getRecentMessages(Long pinId){
        String key = "chat:pin:" + pinId;
        List<String> rawMessages = redisTemplate.opsForList().range(key, 0, -1);
        if(rawMessages == null) return Collections.emptyList();

        return rawMessages.stream()
                .map(json -> {
                    try{
                        return objectMapper.readValue(json, ChatMessageDTO.class);
                    } catch (JsonProcessingException e){
                        log.error("메세지 역직렬화 실패", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
