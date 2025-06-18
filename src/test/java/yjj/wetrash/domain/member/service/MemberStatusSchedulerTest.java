package yjj.wetrash.domain.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberStatusSchedulerTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberStatusScheduler memberStatusScheduler;

    @Test
    void reactivateSuspendedMembersTest(){
        //given
        LocalDateTime suspendedAt = LocalDateTime.now().minusDays(15);
        Member suspendedMember = Member.createSuspendedMemberForTest(suspendedAt);
        when(memberRepository.findAllByMemberStatusAndSuspendedAtBefore(
                eq(MemberStatus.SUSPENDED),
                any(LocalDateTime.class)
        )).thenReturn(List.of(suspendedMember));
        //when
        memberStatusScheduler.reactivateSuspendedMembers();
        //then
        Assertions.assertThat(suspendedMember.getMemberStatus()).isEqualTo(MemberStatus.NORMAL);
    }

}
