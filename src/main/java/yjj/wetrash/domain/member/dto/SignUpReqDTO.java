package yjj.wetrash.domain.member.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.entity.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpReqDTO {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해주세요.")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;


    public Member toEntity(String encPassword){
        return Member.builder()
                .email(email)
                .password(encPassword)
                .nickname(nickname)
                .provider("null") //자체 회원가입
                .profile("null") //임시
                .role(Role.USER)
                .memberStatus(memberStatus)
                .build();
    }

}

