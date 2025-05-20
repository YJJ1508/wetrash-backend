package yjj.wetrash.domain.chat.service;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yjj.wetrash.domain.chat.dto.ChatMessageReportReqDTO;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.exception.ChatErrorCode;
import yjj.wetrash.domain.chat.repository.ChatMessageReportRepository;
import yjj.wetrash.domain.chat.repository.ChatMessageRepository;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.enums.ReportReason;
import yjj.wetrash.global.exception.CustomException;

@SpringBootTest
@Transactional
public class ChatReportServiceTest {

    @Autowired
    private ChatReportService chatReportService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatMessageReportRepository chatMessageReportRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    @DisplayName("메세지 신고 시 저장 test")
    public void saveReportMessage(){
        //given
        Member member = Member.builder().email("test@test.com").nickname("별명").build();
        memberRepository.save(member);
        ChatMessage chatMessage = ChatMessage.builder().pinId(1L).message("셋업").sender(member).build();
        chatMessageRepository.save(chatMessage);
        ChatMessageReportReqDTO reportDTO = new ChatMessageReportReqDTO(chatMessage.getId(), ReportReason.HATE_SPEECH);

        //when
        chatReportService.saveReportMessage("test@test.com", reportDTO);

        //then
        Assertions.assertThat(chatMessage.getReportCount()).isEqualTo(1);
    }
    @Test
    @DisplayName("신고 저장_중복 신고 test")
    public void test_duplicate(){
        //given
        Member member = Member.builder().email("test@test.com").nickname("별명").build();
        memberRepository.save(member);
        ChatMessage chatMessage = ChatMessage.builder().pinId(1L).message("셋업").sender(member).build();
        chatMessageRepository.save(chatMessage);
        ChatMessageReportReqDTO reportDTO = new ChatMessageReportReqDTO(chatMessage.getId(), ReportReason.HATE_SPEECH);
        chatReportService.saveReportMessage("test@test.com", reportDTO);

        //when&then
        Assertions.assertThatThrownBy(() -> chatReportService.saveReportMessage("test@test.com", reportDTO))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ChatErrorCode.CHAT_MESSAGE_ALREADY_REPORTED.getMessage());
    }


}
