package yjj.wetrash.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    boolean existsByEmail(String email); //중복 여부 검사: 회원가입 시 중복여부검사
    Optional<Member> findByEmail(String email); //회원 조회
    //List<Member> findAll(String email); //전체 회원 조회

    boolean existsByNickname(String nickname); // 닉네임 중복 검사
    // 닉네임 회원 조회
    Optional<Member> findByNickname(String nickname);

}
