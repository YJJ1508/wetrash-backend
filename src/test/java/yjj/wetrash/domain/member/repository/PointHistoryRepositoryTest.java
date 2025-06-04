package yjj.wetrash.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.global.config.QuerydslConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
public class PointHistoryRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUpMembersAndPoints(){
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        LocalDateTime withinLastMonth = LocalDateTime.now().minusDays(15);
        //2달전: 포인트 높음
        for (int i = 1; i <= 10; i++){
            Member member = memberRepository.save(
                    Member.builder()
                            .nickname("oldMember" + i)
                            .email("old" + i + "@test.com")
                            .build()
            );
            PointHistory pointHistory = PointHistory.builder()
                            .member(member)
                            .point(1000*i)
                            .build();
            pointHistory.setCreatedAt(twoMonthsAgo);
            pointHistoryRepository.save(pointHistory);
        }
        //1달 이내: 포인트 낮음
        for (int i = 0; i<=10; i++){
            Member member = memberRepository.save(
                    Member.builder()
                            .email("recent" + i + "@test.com")
                            .nickname("recentMember" + i)
                            .build()
            );
            PointHistory pointHistory = PointHistory.builder()
                    .member(member)
                    .point(1000*i)
                    .build();
            pointHistory.setCreatedAt(withinLastMonth);
            pointHistoryRepository.save(pointHistory);
        }
    }

    @Test
    @DisplayName("총 20명 중 10명 2달전 포인트 -> 더 높게" + "나머지 10명은 포인트 -> 더 낮게" +
                    "결과: 지난달 뒤의 10명만 나오는지 TEST")
    void findTopMembersByPointInLastMonthTest(){
        //given
        LocalDateTime start = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().withDayOfMonth(1).minusDays(1).atTime(23,59,59);
        //when
        List<Object[]> top10 = pointHistoryRepository.findTopMembersByPointInLastMonth(start, end);
        //then
        //한달 전 멤버인지 확인
        List<String> nicknames = top10.stream().map(r -> (String) r[1]).toList();
        Assertions.assertThat(nicknames).allMatch(n -> n.startsWith("recentMember"));
        //포인트 내림차순 정렬 확인
        List<Long> points = top10.stream().map(r -> ((Number) r[2]).longValue()).toList();
        Assertions.assertThat(points).isSortedAccordingTo(Comparator.reverseOrder());
    }

}
