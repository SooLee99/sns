package com.example.sns.exception;

import com.example.sns.controller.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// CustomAuthenticationEntryPoint클래스: Spring Security의 AuthenticationEntryPoint를 구현한 커스텀 클래스입니다.
// Spring Security는 HTTP 요청을 처리하는 과정에서 인증 오류(Authentication Error)가 발생할 경우,
// AuthenticationEntryPoint를 통해 처리합니다.
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    // commence 메소드: 인증이 실패할 때 호출되는 메소드입니다.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
    }
}