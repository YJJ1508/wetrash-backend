package yjj.wetrash.domain.member.dto.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileResDTO {
    private Long memberId;
    private String email;
    private String nickname;
    private String provider;
    private String profile;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private MemberStatus memberStatus;

    public static MemberProfileResDTO from(Member member){
        return MemberProfileResDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .provider(member.getProvider())
                .profile(member.getProfile())
                .createdAt(member.getCreatedAt())
                .memberStatus(member.getMemberStatus())
                .build();
    }
}
