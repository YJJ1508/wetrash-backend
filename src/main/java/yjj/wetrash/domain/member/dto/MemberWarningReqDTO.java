package yjj.wetrash.domain.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
//관리자: 사용자 경고 처리
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWarningReqDTO {
    private String email;
}
