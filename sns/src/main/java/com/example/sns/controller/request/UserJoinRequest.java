package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter             // getter 메서드를 자동으로 생성합니다.
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 자동으로 생성합니다.

// UserJoinRequest 클래스 : 회원가입 시 RequestBody로 데이터를 받을 때 사용하는 클래스.
public class UserJoinRequest {
    private String userName;    // 회원가입 요청을 하는 사용자의 이름을 저장합니다.
    private String password;    // 회원가입 요청을 하는 사용자의 비밀번호를 저장합니다.
}
