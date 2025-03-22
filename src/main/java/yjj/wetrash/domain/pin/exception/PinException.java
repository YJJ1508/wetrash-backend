package yjj.wetrash.domain.pin.exception;

import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.exception.ErrorCode;

public class PinException extends CustomException {
    public PinException(PinErrorCode errorCode) {
        super(errorCode);
    }
}
