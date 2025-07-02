package yjj.wetrash.domain.member.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.entity.PointHistory.PointReason;
import yjj.wetrash.domain.pin.entity.Pin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    boolean existsByMemberAndPinAndPointReason(Member member, Pin pin, PointReason pointReason);

    Optional<PointHistory> findByMemberAndPinAndPointReason(Member member, Pin pin, PointReason pointReason);

    List<PointHistory> findAllByMember(Member member);

    //nativeQuery 사용
    @Query(value = """
        SELECT m.member_id, m.nickname, SUM(p.point) as total 
        FROM point_history p
        JOIN member m ON p.member_id = m.member_id
        WHERE p.created_at BETWEEN :start AND :end
        GROUP BY m.member_id, m.nickname
        HAVING SUM(p.point) > 0
        ORDER BY total DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> findTopMembersByPointInLastMonth(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    Optional<PointHistory> findByPin(Pin pin);
}
