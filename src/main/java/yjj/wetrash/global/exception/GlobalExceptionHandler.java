package yjj.wetrash.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice //controller에서의 오류를 감지한다.
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", ex.getErrorCode().getStatus().value());
        errorResponse.put("message", ex.getErrorCode().getMessage());

        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(errorResponse);
    }

}
