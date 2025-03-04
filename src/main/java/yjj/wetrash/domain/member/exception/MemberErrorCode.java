package yjj.wetrash.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    //CONFLICT(409) 이미 존재 하는 값일 때
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),

    //NOT_FOUND(404) 존재 하지 않는 값일 때
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "세션이 만료되었습니다. 다시 로그인해주세요."),

    //UNAUTHORIZED(401) 값이 일치 하지 않는 경우
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 값입니다. 다시 로그인해주세요.");

    private final HttpStatus status;
    private final String message;
}
