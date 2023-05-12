package com.example.sns.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter                     // 클래스 내의 모든 필드에 대해 getter 메서드를 자동 생성해줍니다.
@RequiredArgsConstructor    // 클래스 내에 정의된 모든 final 필드를 파라미터로 받는 생성자를 생성해줍니다.

// ErrorCode: API에서 예외 발생 시 해당 '에러 코드'와 '에러 메시지'를 응답으로 반환하는 등의 역할을 수행합니다.
public enum ErrorCode {

    // 회원가입을 할 때, 유저 아이디가 중복된 경우
    // status(상태 코드) => CONFLICT(409)
    // message(에러 메시지) => "User name is duplicated"
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),

    // 로그인을 할 때, 유저를 찾을 수 없는 경우
    // status(상태 코드) => NOT_FOUND(404)
    // message(에러 메시지) => "User not founded"
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),

    // 로그인을 할 때, 비밀번호가 잘못된 경우
    // status(상태 코드) => UNAUTHORIZED(401)
    // message(에러 메시지) => "Password is invalid"
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),

    // 로그인을 할 때, 토큰이 잘못된 경우
    // status(상태 코드) => UNAUTHORIZED(401)
    // message(에러 메시지) => "Token is invalid"
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),

    // 게시글을 찾을 수 없는 경우
    // status(상태 코드) => NOT_FOUND(404)
    // message(에러 메시지) => "Post not founded"
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not founded"),

    // 권한이 잘못된 경우
    // status(상태 코드) => UNAUTHORIZED(401)
    // message(에러 메시지) => "Permission is invalid"
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),

    // 서버 내부 오류가 발생한 경우
    // status(상태 코드) => INTERNAL_SERVER_ERROR(500)
    // message(에러 메시지) => "Internal server error"
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    // 데이터베이스 오류가 발생한 경우
    // status(상태 코드) => INTERNAL_SERVER_ERROR(500)
    // message(에러 메시지) => "Error occurs in database"
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurs in database"),

    // 이미 좋아요를 누른 상태에서 중복으로 좋아요를 눌렀을 경우
    // status(상태 코드) => CONFLICT(409)
    // message(에러 메시지) => "User already liked the post"
    ALREADY_LIKED(HttpStatus.CONFLICT, "User already liked the post");

    private final HttpStatus status;    // 에러 코드에 대한 HTTP 상태 코드를 나타내는 필드
    private final String message;       // 에러 코드에 대한 메시지를 나타내는 필드
}