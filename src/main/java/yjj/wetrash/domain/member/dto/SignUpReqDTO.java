package yjj.wetrash.domain.member.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.Role;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder
    public SignUpReqDTO(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toEntity(String encPassword){
        return Member.builder()
                .email(email)
                .password(encPassword)
                .nickname(nickname)
                .role(Role.USER)
                .build();
    }

}

