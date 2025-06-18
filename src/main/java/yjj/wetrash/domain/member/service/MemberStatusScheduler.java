package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberStatusScheduler {

    private final MemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void reactivateSuspendedMembers(){
        List<Member> members = memberRepository.findAllByMemberStatusAndSuspendedAtBefore(
                MemberStatus.SUSPENDED,
                LocalDateTime.now().minusDays(15)
        );
        members.forEach(Member::reactivateStatus);
        memberRepository.saveAll(members);
    }

}
