package yjj.wetrash.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.Role;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResDTO { //상단바 ui용
    private String nickname;
    private Long memberId;
    @JsonFormat(shape = JsonFormat.Shape.STRING) //없어도 되긴함
    private Role role;

    public static UserInfoResDTO of(Member member){
        return UserInfoResDTO.builder()
                .nickname(member.getNickname())
                .memberId(member.getId())
                .role(member.getRole())
                .build();
    }
}
