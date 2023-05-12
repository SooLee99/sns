package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter                 // 해당 클래스의 모든 필드에 대한 getter 메소드를 생성합니다.
@AllArgsConstructor     // 해당 클래스에 대한 모든 필드를 인자로 받는 생성자를 생성합니다.

// UserJoinRequest 클래스 : 로그인을 하는 경우, RequestBody로 데이터를 받을 때 사용하는 클래스.
public class UserLoginRequest {
    private String name;        // 로그인에 사용되는 사용자 이름을 저장하는 필드입니다.
    private String password;    // 로그인에 사용되는 비밀번호를 저장하는 필드입니다.
}
