package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING) //string타입으로 변환하여 db저장한다.
    private Role role;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //권한

    //소셜


    //즐겨찾기

    //코멘트

    //요청

    //댓글

    //게시글

    //좋아요


    @Builder
    public Member(String email, String password, String nickname, Role role){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

}
