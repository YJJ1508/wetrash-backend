package yjj.wetrash.domain.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yjj.wetrash.global.exception.ErrorCode;
@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅 메세지 입니다."),

    CHAT_MESSAGE_ALREADY_REPORTED(HttpStatus.ALREADY_REPORTED, "이미 신고한 메세지입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
