package yjj.wetrash.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.chat.entity.ChatMessage;
import yjj.wetrash.domain.chat.entity.ChatMessageReport;
import yjj.wetrash.domain.member.entity.Member;

import java.util.List;

public interface ChatMessageReportRepository extends JpaRepository<ChatMessageReport, Long> {

    boolean existsByReporterAndReportedMessage(Member reporter, ChatMessage reportedMessage);

    List<ChatMessageReport> getChatMessageReportByReportedMessage(ChatMessage chatMessage);

}
