package yjj.wetrash.domain.member.dto;

import lombok.*;
import yjj.wetrash.domain.member.entity.Member;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResDTO {
    private String nickname;

    public static UserInfoResDTO of(Member member){
        return UserInfoResDTO.builder()
                .nickname(member.getNickname())
                .build();
    }
}
