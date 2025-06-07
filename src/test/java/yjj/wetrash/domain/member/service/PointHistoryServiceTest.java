package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import yjj.wetrash.domain.member.dto.PointRankingResDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
import yjj.wetrash.global.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional @Rollback
public class PointHistoryServiceTest {
    
//    @Mock
//    private MemberRepository memberRepository;
//    @Mock
//    private PointHistoryRepository pointHistoryRepository;
//
//    @InjectMocks
//    private PointHistoryService pointHistoryService;

    //DateUtil 때문에 springbootTest 로
    @Autowired
    private PointHistoryService pointHistoryService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUpForTop10MembersInLastMonth(){
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        LocalDateTime withinLastMonth = LocalDateTime.now().minusDays(15);
        //2달전: 포인트 높음
        for (int i = 1; i <= 10; i++){
            Member member = memberRepository.save(
                    Member.builder()
                            .nickname("oldMember0" + i)
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
        for (int i = 1; i <= 10; i++){
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
    void calculateTier(){
        //given
        Member member1 = Member.builder().email("test@test.com").nickname("member1")
                .totalPoint(4000).build();
        Member member2 = Member.builder().email("test2@test.com").nickname("member2")
                .totalPoint(5).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        String member1_tier = pointHistoryService.calculateTier(member1);
        String member2_tier = pointHistoryService.calculateTier(member2);
        //then
        Assertions.assertThat(member1_tier).isEqualTo("GRANDMASTER");
        Assertions.assertThat(member2_tier).isEqualTo("BRONZE");
    }

    @Test
    void getTop10MembersInLastMonthTest(){ //얘를 DateUtil 때문에 springbootTest 로 test
        //when
        List<PointRankingResDTO> top10DTO = pointHistoryService.getTop10MembersInLastMonth();
        //then
        List<String> nicknames = top10DTO.stream().map(PointRankingResDTO::getNickname).toList();
        Assertions.assertThat(nicknames).allMatch(n -> n.startsWith("recentMember"));
    }

}
