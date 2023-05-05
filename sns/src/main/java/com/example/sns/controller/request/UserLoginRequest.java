package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

// UserJoinRequest 클래스 : 로그인을 하는 경우, RequestBody로 데이터를 받을 때 사용하는 클래스.
@Getter
@AllArgsConstructor
public class UserLoginRequest {
    private String userName;
    private String password;
}
