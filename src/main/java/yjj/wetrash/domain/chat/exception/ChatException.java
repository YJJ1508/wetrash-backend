package yjj.wetrash.domain.chat.exception;

import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.exception.ErrorCode;

public class ChatException extends CustomException {
    public ChatException(ChatErrorCode errorCode) {
        super(errorCode);
    }
}
