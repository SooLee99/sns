package com.example.sns.controller;

import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
// => 클래스 내부에 선언된 모든 final 필드나 @NonNull 필드에 대한 생성자가 자동으로 만들어지므로 코드의 양을 줄일 수 있습니다.
//    userService 클래스를 사용하기 위한 Lombok 어노테이션.
public class UserController {

    private final UserService userService;

    // TODO :
    @PostMapping
    public void join(){
        // UserService 클래스의 join() 서비스를 진행.
        userService.join("userName", "password");

    }

}
