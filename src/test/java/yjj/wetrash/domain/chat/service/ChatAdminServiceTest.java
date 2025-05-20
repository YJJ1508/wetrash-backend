package yjj.wetrash.domain.chat.service;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import yjj.wetrash.domain.chat.dto.ChatMessageAdminDTO;
import yjj.wetrash.domain.chat.dto.ChatMessageReportReqDTO;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.domain.chat.repository.ChatMessageReportRepository;
import yjj.wetrash.domain.chat.repository.ChatMessageRepository;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.config.QuerydslConfig;
import yjj.wetrash.global.enums.ReportReason;

import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
public class ChatAdminServiceTest {

    @Autowired
    private ChatAdminService chatAdminService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatReportService chatReportService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        //1.test 회원 생성
        Member member1 = Member.builder().email("test@test.com").nickname("tester").build();
        Member member2 = Member.builder().email("test2@test.com").nickname("test2").build();
        Member member3 = Member.builder().email("test3@test.com").nickname("test3").build();
        Member member4 = Member.builder().email("test4@test.com").nickname("test4").build();
        Member member5 = Member.builder().email("test5@test.com").nickname("test5").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        //2.채팅 메세지 + 신고 생성
        ChatMessage message = ChatMessage.builder().pinId(1L).message("신고될 메세지 test").sender(member1).build();
        chatMessageRepository.save(message);

        //3.신고 생성 (count_limit 이상)
        ChatMessageReportReqDTO report1 = new ChatMessageReportReqDTO(message.getId(), ReportReason.HATE_SPEECH);
        ChatMessageReportReqDTO report2 = new ChatMessageReportReqDTO(message.getId(), ReportReason.HATE_SPEECH);
        ChatMessageReportReqDTO report3 = new ChatMessageReportReqDTO(message.getId(), ReportReason.HATE_SPEECH);
        ChatMessageReportReqDTO report4 = new ChatMessageReportReqDTO(message.getId(), ReportReason.HATE_SPEECH);
        ChatMessageReportReqDTO report5 = new ChatMessageReportReqDTO(message.getId(), ReportReason.HATE_SPEECH);
        chatReportService.saveReportMessage(member1.getEmail(), report1);
        chatReportService.saveReportMessage(member2.getEmail(), report2);
        chatReportService.saveReportMessage(member3.getEmail(), report3);
        chatReportService.saveReportMessage(member4.getEmail(), report4);
        chatReportService.saveReportMessage(member5.getEmail(), report5);
    }


    @Test
    @DisplayName("신고 5회 이상 chat admin 조회 띄우기 test")
    public void getReporterMessagesGreaterThanCountTest(){
        //given
        // setUp()
        //when
        List<ChatMessageAdminDTO> result = chatAdminService.getReportMessagesGreaterThanCount();
        //then
        Assertions.assertThat(result).isNotEmpty();
        ChatMessageAdminDTO dto = result.get(0);
        Assertions.assertThat(dto.getMessage()).isEqualTo("신고될 메세지 test");
    }

}
