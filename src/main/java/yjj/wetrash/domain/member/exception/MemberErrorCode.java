package yjj.wetrash.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    //FORBIDDEN(403) 정지 회원
    SUSPENDED_USER(HttpStatus.FORBIDDEN, "정지된 회원으로, 일부 기능이 제한됩니다."),
    WARNING_USER(HttpStatus.FORBIDDEN, "경고 상태인 사용자로, 일부 기능이 제한됩니다."),

    //CONFLICT(409) 이미 존재 하는 값일 때
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "존재하는 이메일입니다."),

    //NOT_FOUND(404) 존재 하지 않는 값일 때
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "세션이 만료되었습니다. 다시 로그인해주세요."),
    POINT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 포인트 기록입니다."),

    //UNAUTHORIZED(401) 값이 일치 하지 않는 경우
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 값입니다. 다시 로그인해주세요.");

    private final HttpStatus status;
    private final String message;
}
