package yjj.wetrash.domain.chat.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import yjj.wetrash.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    //void deleteByCreatedAtLessThan(LocalDateTime cutoffTime);

    @Modifying // 삭제 수행시
    @Transactional
    @Query("DELETE FROM ChatMessage m WHERE m.createdAt < :cutoffTime")
    void deleteOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

    List<ChatMessage> findByReportCountGreaterThanEqual(int count);
}
