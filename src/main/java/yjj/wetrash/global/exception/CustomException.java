package yjj.wetrash.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }
    public String getMessage() {
        return errorCode.getMessage();
    }
}
