package yjj.wetrash.domain.pin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;
@Getter
@RequiredArgsConstructor
public enum PinErrorCode implements ErrorCode {

    //409
    PIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 핀입니다."),
    //404
    PIN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 핀입니다."),
    PIN_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 핀 리뷰입니다."),
    //403
    PIN_REVIEW_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 작성자만 수정할 수 있습니다."),
    PIN_REVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 작성자만 삭제할 수 있습니다."),

    ;


    private final HttpStatus status;
    private final String message;
}
