package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter                 // 클래스 내 모든 필드에 대한 getter 메소드를 생성해줍니다.
@AllArgsConstructor     // 클래스 내 모든 필드를 파라미터로 받는 생성자를 생성해줍니다.
public class SnsApplicationException extends RuntimeException {

    private ErrorCode errorCode;    // 예외 객체에 해당하는 에러 코드를 저장하는 필드
    private String message;         // 예외 객체에 대한 추가적인 메시지를 저장하는 필드


    // 에러 코드만을 파라미터로 받는 생성자입니다.
    public SnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        // 예외 객체에 추가적인 메시지가 없으면, 에러 코드에 정의된 메시지를 그대로 반환합니다.
        if (message == null) {
            return errorCode.getMessage();
        }

        // 예외 객체에 추가적인 메시지가 있으면, 해당 메시지를 에러 코드에 정의된 메시지와 함께 반환합니다.
        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}