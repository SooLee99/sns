package com.example.sns.cofiguration;

import com.example.sns.cofiguration.filter.JwtTokenFilter;
import com.example.sns.exception.CustomAuthenticationEntryPoint;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration: 이 클래스가 구성을 위한 구성 클래스임을 명시합니다.
@Configuration

// @EnableWebSecurity: Spring Security를 사용할 것임을 명시합니다.
@EnableWebSecurity

// @RequiredArgsConstructor: 필드 주입을 위한 생성자를 생성합니다.
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    @Override
    // 이 함수는 보안 필터 체인의 일부로 웹 보안을 구성합니다.
    // 여기에서는 "/api/"가 아닌 경로의 요청은 무시하도록 설정합니다.
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().regexMatchers("^(?!/api/).*");
    }

    @Override
    // protected void configure(HttpSecurity http) throws Exception: HTTP 보안을 구성합니다.
    // 여기에서는 CSRF 보호 기능을 비활성화하고 "/api//users/join", "/api//users/login" 요청은 인증 없이 허용하고,
    // "/api/**" 요청은 인증이 필요하도록 설정합니다. 또한, JWT 인증 필터를 등록하고 예외 처리 방법을 지정합니다.
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()

                // 스프링 시큐리티가 기본적으로 사용하는 HTTP 세션 대신 STATELESS 방식으로 인증을 처리하도록 지정합니다.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // JWT 토큰 인증 필터를 추가합니다.
                // JwtTokenFilter는 UsernamePasswordAuthenticationFilter보다 먼저 실행되도록 설정합니다.
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)

                // 인증 오류 발생시 처리를 위한 예외 처리 핸들러를 지정합니다.
                // CustomAuthenticationEntryPoint는 인증 오류 시 응답을 처리하기 위한 사용자 지정 클래스입니다.
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }
}