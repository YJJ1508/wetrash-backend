package yjj.wetrash.domain.pin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;
@Getter
@RequiredArgsConstructor
public enum PinErrorCode implements ErrorCode {

    PIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 핀입니다.");

    private final HttpStatus status;
    private final String message;
}
