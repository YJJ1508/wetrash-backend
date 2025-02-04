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

    //UNAUTHORIZED(401) 값이 일치 하지 않는 경우
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}
