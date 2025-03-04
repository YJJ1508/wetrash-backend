package yjj.wetrash.global.security.jwt.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JwtTokenDTO {
    private String type;  //1.alone 2.both
    private String accessToken;
    private String refreshToken;  //HttpOnly 쿠키로 전송
    private Long accessTokenExpiresIn;

    public static JwtTokenDTO of(String type, String accessToken, String refreshToken, Long accessTokenExpiresIn){
        return JwtTokenDTO.builder()
                .type(type)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .build();
    }
}
