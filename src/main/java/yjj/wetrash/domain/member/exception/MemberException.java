package yjj.wetrash.domain.member.exception;

import yjj.wetrash.global.exception.CustomException;

public class MemberException extends CustomException{
    public MemberException(MemberErrorCode errorCode){
        super(errorCode);
    }
}
