package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.controller.response.AlarmResponse;
import com.example.sns.controller.response.Response;
import com.example.sns.controller.response.UserJoinResponse;
import com.example.sns.controller.response.UserLoginResponse;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
// 로깅: 애플리케이션의 실행 과정에서 중요한 정보를 기록하고 추적하기 위해 사용하는 것입니다.
//   @Slf4j 어노테이션을 클래스에 추가하면, 해당 클래스에 대한 로그 객체가 자동으로 생성됩니다.
//   이 객체를 사용하여 로그 메시지를 출력할 수 있으며, 이를 위해 다양한 로그 레벨(trace, debug, info, warn, error 등)을 사용할 수 있습니다.
//   이를 통해, 애플리케이션의 실행 과정에서 발생하는 문제를 신속하게 파악하고 해결할 수 있습니다.

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
// => 클래스 내부에 선언된 모든 final 필드나 @NonNull 필드에 대한 생성자가 자동으로 만들어지므로 코드의 양을 줄일 수 있습니다.
//    userService 클래스를 사용하기 위한 Lombok 어노테이션.
public class UserController {

    private final UserService userService;
    //private final AlarmService alarmService;

    @PostMapping("/join")
    // join 메소드 : 회원가입 요청을 처리하는 메소드
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        // UserJoinRequest 객체를 매개변수로 받아 UserService 클래스의 join() 메서드를 호출하여 회원가입을 진행. 
        // 그리고 회원가입 결과를 Response<UserJoinResponse> 객체에 담아 반환
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getUserName(), request.getPassword())));
    }

    @PostMapping("/login")
    // login 메소드 : 로그인 요청을 처리하는 메소드
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        // UserLoginRequest 객체를 매개변수로 받아 UserService 클래스의 login() 메서드를 호출하여 로그인을 진행합니다.
        String token = userService.login(request.getName(), request.getPassword());

        // 그리고 로그인 결과를 Response<UserLoginResponse> 객체에 담아 반환합니다.
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    // alarm 메소드 : 알람 목록 요청을 처리하는 메소드
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        // Pageable 객체와 Authentication 객체를 매개변수로 받아 UserService 클래스의 alarmList() 메서드를 호출하여
        // 해당 유저의 알람 목록을 조회합니다.
        // 그리고 조회된 알람 목록을 Page<AlarmResponse> 객체에 담아 반환합니다.
        return Response.success(userService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarm));
    }
}
