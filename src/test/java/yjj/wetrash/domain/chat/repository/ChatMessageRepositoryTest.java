package yjj.wetrash.domain.chat.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import yjj.wetrash.WetrashBackendApplication;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.global.config.QuerydslConfig;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class) //datajpatest가 전체 repository를 스캔해서 jpaQueryFactory 필요해짐
public class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    @DisplayName("채팅 보관 일주일 후 삭제 테스트")
    void deleteOlderThan_cutoffDate(){
        //given 객체 넣어주기
        LocalDateTime now = LocalDateTime.now();
        ChatMessage chat1 = ChatMessage.builder().message("첫번째_날짜 지남").createdAt(now.minusMonths(1)).build();
        ChatMessage chat2 = ChatMessage.builder().message("두번째_날짜 안 지남").createdAt(now).build();
        chatMessageRepository.save(chat1);
        chatMessageRepository.save(chat2);
        //when
        chatMessageRepository.deleteOlderThan(now);
        //then
        List<ChatMessage> messages = chatMessageRepository.findAll();
        Assertions.assertThat(messages.size()).isEqualTo(1);
        Assertions.assertThat(messages).contains(chat2);
        Assertions.assertThat(messages).doesNotContain(chat1);
    }

}
