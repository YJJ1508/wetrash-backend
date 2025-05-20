package yjj.wetrash.domain.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.chat.dto.ChatMessageAdminDTO;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatAdminService {

    private final int COUNT_LIMIT = 5; //COUNT 이상 신고 댓글만 조회
    private final ChatService chatService;
    private final ChatReportService chatReportService;

    @Transactional
    public List<ChatMessageAdminDTO> getReportMessagesGreaterThanCount(){
        return chatService.getReportedMessagesForAdmin(COUNT_LIMIT)
                .stream()
                .map(chatMessage -> {
                    List<ChatMessageReport> reports = chatReportService.getReportedMessagesForAdmin(chatMessage);
                    return ChatMessageAdminDTO.from(chatMessage, reports);
                })
                .collect(Collectors.toList());
    }


}
