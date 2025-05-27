package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.pin.entity.PinReview;

import java.time.LocalDateTime;
import java.util.List;

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

    //즐겨찾기

    //후기
//    @OneToMany(mappedBy = "member")
//    private List<PinReview> pinReviews;

    //게시글

    //좋아요


    @Builder
    public Member(String email, String password, String nickname, Role role, String profile, String provider, MemberStatus memberStatus){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.role = role;
        this.provider = provider;
        this.memberStatus = memberStatus;
    }

    public MemberReputation createReputation(){
        return new MemberReputation(this); //회원 생성 시 평판 생성.
    }
    public void setStatusToWarning(){
        this.memberStatus = MemberStatus.WARNING;
    }
    public void setStatusToBan(){
        this.memberStatus = MemberStatus.BANNED;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfile(String profile){
        this.profile = profile;
    }

}
