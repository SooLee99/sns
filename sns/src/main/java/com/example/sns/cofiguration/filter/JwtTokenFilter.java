package com.example.sns.cofiguration.filter;

import com.example.sns.model.User;
import com.example.sns.service.UserService;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @Slf4j 어노테이션 : Lombok 라이브러리의 어노테이션으로, 자동으로 로깅 객체를 생성해줍니다.
@Slf4j

// @RequiredArgsConstructor 어노테이션 : Lombok 라이브러리의 어노테이션으로,
// 해당 클래스의 모든 final 필드를 매개변수로 받는 생성자를 자동으로 생성해줍니다.
@RequiredArgsConstructor

// JwtTokenFilter 클래스 : JWT 토큰을 검증하는 필터입니다.
// OncePerRequestFilter 클래스를 상속받아, 모든 요청에 대해 한 번씩만 실행되도록 구현되어 있습니다.
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    // doFilterInternal 메서드 : 모든 요청에 대해 실행되는 메서드로,
    // HttpServletRequest, HttpServletResponse, FilterChain 객체를 매개변수로 받습니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get header 변수 : 요청 헤더에서 Authorization 헤더를 가져와 저장합니다.
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // header 변수의 검증 : Authorization 헤더가 존재하지 않거나, Bearer 문자열로 시작하지 않을 경우 에러 로그를 출력하고, 다음 필터로 이동합니다.
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header. header is null or invalid {}", request.getRequestURL());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // token 변수 : Bearer 문자열 이후의 토큰 값을 추출하여 저장합니다.
            final String token = header.split(" ")[1].trim();

            // token 변수의 검증 : 추출된 토큰이 만료되었을 경우 에러 로그를 출력하고, 다음 필터로 이동합니다.
            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("Key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            // userName 변수 : 토큰에서 추출한 유저 이름을 저장합니다.
            String userName = JwtTokenUtils.getUserName(token, key);

            // userService 객체를 사용하여 userName에 해당하는 유저 정보를 조회합니다.
            User user = userService.loadUserByUserName(userName);

            // authentication 객체 : 유저 정보를 바탕으로 생성된 UsernamePasswordAuthenticationToken 객체를 저장합니다.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // SecurityContextHolder 객체에 authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }// 에러 발생 시 에러 로그를 출력하고, 다음 필터로 이동합니다.
        catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        // 다음 필터로 이동합니다.
        filterChain.doFilter(request, response);
    }
}