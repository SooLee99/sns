package com.example.sns.cofiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 스프링 설정 파일임을 나타내고 있습니다.
@Configuration
// 스프링 시큐리티에서 제공하는 BCryptPasswordEncoder를 빈으로 등록하는 설정 클래스입니다.
public class SecurityConfig {

    @Bean // @Bean 어노테이션을 사용하여 encodePassword() 메서드를 빈으로 등록하고 있습니다.

    // BCryptPasswordEncoder는 비밀번호를 안전하게 저장하기 위해 사용되는 인코더 클래스 중 하나입니다.
    // 비밀번호를 해시 함수를 통해 암호화하고, 랜덤한 salt를 추가하여 해시 결과를 생성합니다.
    // 이렇게 생성된 해시 결과는 복호화가 불가능하기 때문에, 안전하게 저장할 수 있습니다.
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}