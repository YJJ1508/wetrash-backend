package yjj.wetrash.domain.member.entity;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.MemberReputationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberReputationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberReputationRepository memberReputationRepository;

    @Test
    @DisplayName("5회 이상 경고 누적 시 total_warn 1+")
    void 어드민_경고_test(){
        //given
        Member member = Member.builder().email("test@test.com").nickname("test").password("12").build();
        memberRepository.save(member);
        MemberReputation mr = new MemberReputation(member);
        memberReputationRepository.save(mr);
        //when
        for (int i = 0; i<5; i++){
            mr.addAdminWarning();
        }
        //then
        Assertions.assertThat(mr.getTotalWarnCount()).isEqualTo(1); //경고 적립 1회
        Assertions.assertThat(mr.getWarningCount()).isEqualTo(0); //초기화 확인
        assertEquals(MemberStatus.WARNING, member.getMemberStatus());
    }

    @Test
    @DisplayName("경고 3번 누적시 total_warn 3번 -> BAN")
    void 어드민_밴_test(){
        //given
        Member member = Member.builder().email("test@test.com").nickname("test").password("12").build();
        memberRepository.save(member);
        MemberReputation mr = new MemberReputation(member);
        memberReputationRepository.save(mr);
        //when
        for (int i = 0; i<15; i++){
            mr.addAdminWarning();
        }
        //then
        assertEquals(MemberStatus.BANNED, member.getMemberStatus());
    }

}
