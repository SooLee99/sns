package com.example.sns.service;

import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
// => 클래스 내부에 선언된 모든 final 필드나 @NonNull 필드에 대한 생성자가 자동으로 만들어지므로 코드의 양을 줄일 수 있습니다.
//    userService 클래스를 사용하기 위한 Lombok 어노테이션.
public class UserService {

    private final UserEntityRepository userEntityRepository;

    // TODO : implement
    // 실제 회원가입이 실행되는 메소드
    public User join(String userName, String password) {
        // 회원가입하려는 userName으로 회원가입 된 user가 있는지 확인.
        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);

        // 그게 아니라면, 회원가입 진행.
        userEntityRepository.save(new UserEntity());

        return new User();
    }

    // 실제 로그인이 실행되는 메소드
    // Spring Security를 이용해 JWT(JSON Web Token)를 사용.
    // => 로그인 시, 아이디 토큰[암호화된 문자열]을 발행해서 인증함.
    // TODO : implement
    public String login(String userName, String password) {
        // (1) 회원가입 여부 체크.
        // => userEntityRepository에서 검색한 userName이 비어있는 경우, 해당 에러를 반환한다.
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException());

        // (2) 비밀번호 체크.
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException();
        }

        // 토큰 생성.

        return "";  // 추후 변경 예정.
    }
}
