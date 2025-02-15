package yjj.wetrash.global.security.jwt.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*3) //3일
public class RefreshToken {

    @Id
    private String memberId;
    @Indexed
    private String refreshToken;

    @Builder
    public RefreshToken(String refreshToken, String memberId){
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}
