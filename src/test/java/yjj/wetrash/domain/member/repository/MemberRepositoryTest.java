package yjj.wetrash.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.global.config.QuerydslConfig;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void top10Test(){
        //given
        Member member1 = Member.builder().nickname("member1").totalPoint(5000).email("member1@test.com").build();
        Member member2 = Member.builder().nickname("member2").totalPoint(4000).email("member2@test.com").build();
        Member member3 = Member.builder().nickname("member3").totalPoint(4000).email("member3@test.com").build();
        Member member4 = Member.builder().nickname("member4").totalPoint(3000).email("member4@test.com").build();
        Member member5 = Member.builder().nickname("member5").totalPoint(2000).email("member5@test.com").build();
        Member member6 = Member.builder().nickname("member6").totalPoint(1000).email("member6@test.com").build();
        Member member7 = Member.builder().nickname("member7").totalPoint(500).email("member7@test.com").build();
        Member member8 = Member.builder().nickname("member8").totalPoint(300).email("member8@test.com").build();
        Member member9 = Member.builder().nickname("member9").totalPoint(10).email("member9@test.com").build();
        Member member10 = Member.builder().nickname("member10").totalPoint(5).email("member10@test.com").build();
        Member member11 = Member.builder().nickname("member11").totalPoint(1).email("member11@test.com").build();
        memberRepository.saveAll(List.of(
                member1, member2, member3, member4, member5,member6, member7, member8, member9, member10, member11
        ));
        //when
        List<Long> top10 = memberRepository.findMemberIdTop10ByTotalPoint((Pageable) PageRequest.of(0, 10));
        //then
        Assertions.assertThat(top10).containsExactly(
                member1.getId(), member2.getId(), member3.getId(),
                member4.getId(), member5.getId(), member6.getId(),
                member7.getId(), member8.getId(), member9.getId(), member10.getId()
        );
        Assertions.assertThat(top10).doesNotContain(member11.getId());
    }

}
