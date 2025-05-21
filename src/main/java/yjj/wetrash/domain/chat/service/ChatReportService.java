package yjj.wetrash.domain.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.chat.dto.ChatMessageReportReqDTO;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.domain.chat.exception.ChatErrorCode;
import yjj.wetrash.domain.chat.repository.ChatMessageReportRepository;
import yjj.wetrash.domain.chat.repository.ChatMessageRepository;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.exception.CustomException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatReportService {

    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReportRepository chatMessageReportRepository;

    @Transactional
    public void saveReportMessage(String email, ChatMessageReportReqDTO reportDTO){
        //신고한 회원 찾기
        Member reporter =  memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        //메세지 객체 찾기(글쓴이)
        ChatMessage reportedMessage = chatMessageRepository.findById(reportDTO.getChatMessageId())
                .orElseThrow(() -> new CustomException(ChatErrorCode.CHAT_MESSAGE_NOT_FOUND));
        //한 사용자 당 한 번만 신고 가능
        boolean alreadyReported = chatMessageReportRepository.existsByReporterAndReportedMessage(reporter, reportedMessage);
        if (alreadyReported) {
            throw new CustomException(ChatErrorCode.CHAT_MESSAGE_ALREADY_REPORTED);
        }
        //신고 횟수 카운트 +1
        reportedMessage.increaseReportCount();
        //report 객체 생성
        ChatMessageReport chatMessageReport = ChatMessageReport.builder()
                .chatMessage(reportedMessage)
                .reporter(reporter)
                .reason(reportDTO.getReason())
                .build();
        chatMessageReportRepository.save(chatMessageReport);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ChatMessageReport> getReportedMessagesForAdmin(ChatMessage chatMessage){
        return chatMessageReportRepository.getChatMessageReportByReportedMessage(chatMessage);
    }


}
