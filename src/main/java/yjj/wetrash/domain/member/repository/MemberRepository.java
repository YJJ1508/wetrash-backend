package yjj.wetrash.domain.member.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.entity.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    boolean existsByEmail(String email); //중복 여부 검사: 회원가입 시 중복여부검사
    Optional<Member> findByEmail(String email); //회원 조회
    List<Member> findAllByRole(Role role);

    boolean existsByNickname(String nickname); // 닉네임 중복 검사
    // 닉네임 회원 조회
    Optional<Member> findByNickname(String nickname);

    //등급 매기기 위한 top 10 조회
    @Query("SELECT m.id FROM Member m ORDER BY m.totalPoint DESC")
    List<Long> findMemberIdTop10ByTotalPoint(Pageable pageable); //pageable 여기선 limit 대신 하기 위함.

    //회원 top10 조회
    @Query("SELECT m FROM Member m WHERE m.totalPoint > 0 ORDER BY m.totalPoint DESC")
    List<Member> findMemberTop10ByTotalPoint(Pageable pageable);

    List<Member> findAllByMemberStatusAndSuspendedAtBefore(MemberStatus status, LocalDateTime dateTime);
}
