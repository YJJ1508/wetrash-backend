package yjj.wetrash.global.security.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode{
    ILLEGAL_PROVIDER_ID(HttpStatus.FORBIDDEN, "지원하지 않는 OAuth2 Provider입니다.");

    private final HttpStatus status;
    private final String message;
}
