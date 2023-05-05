package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

// UserJoinRequest 클래스 : 회원가입 시 RequestBody로 데이터를 받을 때 사용하는 클래스.
@Getter
@AllArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
}
