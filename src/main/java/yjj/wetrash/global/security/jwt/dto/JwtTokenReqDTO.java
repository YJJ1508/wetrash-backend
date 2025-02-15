package yjj.wetrash.global.security.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenReqDTO {
    private String accessToken;
    private String refreshToken;
}
