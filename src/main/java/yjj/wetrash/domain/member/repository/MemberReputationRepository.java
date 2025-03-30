package yjj.wetrash.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberReputation;

import java.util.List;
import java.util.Optional;

public interface MemberReputationRepository extends JpaRepository<MemberReputation, Long> {

    Optional<MemberReputation> findMemberReputationByMemberEmail(String email);

}
