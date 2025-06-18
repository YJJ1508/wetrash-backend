package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider; //구글,네이버,카카오,자체 로그인

    private String profile;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus memberStatus;

    private LocalDateTime suspendedAt;

    private int totalPoint; //포인트 적립

    @Builder
    public Member(String email, String password, String nickname, Role role, String profile, String provider, MemberStatus memberStatus,
                  int totalPoint){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.role = role;
        this.provider = provider;
        this.memberStatus = memberStatus;
        this.totalPoint = totalPoint;
    }

    public MemberReputation createReputation(){
        return new MemberReputation(this); //회원 생성 시 평판 생성.
    }
    public void setStatusToWarning(){
        this.memberStatus = MemberStatus.WARNING;
    }
    public void setStatusToBan(){
        this.memberStatus = MemberStatus.SUSPENDED;
        this.suspendedAt = LocalDateTime.now();
    }
    public void reactivateStatus(){
        this.memberStatus = MemberStatus.NORMAL;
        this.suspendedAt = null;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfile(String profile){
        this.profile = profile;
    }

    public void addPoint(int point){
        this.totalPoint += point;
    }
    public void subtractPoint(int point){
        this.totalPoint -= point;
    }


    //테스트 전용 생성자
    public static Member createSuspendedMemberForTest(LocalDateTime suspendedAt){
        Member member = new Member();
        member.memberStatus = MemberStatus.SUSPENDED;
        member.suspendedAt = suspendedAt;
        return member;
    }

}
