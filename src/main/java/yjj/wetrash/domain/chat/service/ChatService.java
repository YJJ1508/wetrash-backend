package yjj.wetrash.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.chat.dto.ChatMessageDTO;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.MessageType;
import yjj.wetrash.domain.chat.repository.ChatMessageRepository;

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
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void processMessage(ChatMessageDTO messageDTO){
        //1.timestamp 설정
        messageDTO.setCreatedAt(LocalDateTime.now());
        //enter 일 경우 입장 메세지 셋
        if (messageDTO.getType() == MessageType.ENTER) {
            messageDTO.setMessage(messageDTO.getSender() + "님이 입장하셨습니다.");
        }
        //2.redis 저장 - 30분
        try{
            String key = "chat:pin:"+messageDTO.getPinId();
            String json = objectMapper.writeValueAsString(messageDTO);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.expire(key, Duration.ofMinutes(30)); //TTL 30분 설정
        } catch (JsonProcessingException e){
            log.error("채팅 메세지 json 변환 실패", e);
        }
        //3.신고용 위한 저장 - 일주일~한 달
        if (messageDTO.getType() == MessageType.TALK){
            saveChatMessage(messageDTO);
        }
        //4.핀 채팅방에 broadcast
        String destination = "/sub/pin/" + messageDTO.getPinId();
        messagingTemplate.convertAndSend(destination, messageDTO);
    }
    private void saveChatMessage(ChatMessageDTO messageDTO){
        ChatMessage chatMessage = messageDTO.toEntity(messageDTO);
        chatMessageRepository.save(chatMessage);
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

    // db 저장 기한 정하기
    @Scheduled(cron = "0 0 0 * * *") //매일 자정 실행
    @Transactional
    public void deleteOldChatMessages(){
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        chatMessageRepository.deleteOlderThan(oneWeekAgo);
    }

}
