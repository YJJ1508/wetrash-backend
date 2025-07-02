package yjj.wetrash.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {

    private String nickname;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private MemberStatus memberStatus;
    private String provider;

    public static UserListDTO fromEntity(Member member){
        return UserListDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt().toLocalDate())
                .memberStatus(member.getMemberStatus())
                .provider(member.getProvider())
                .build();
    }

}
