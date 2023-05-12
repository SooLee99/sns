package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Alarm;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.AlarmEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// => 클래스 내부에 선언된 모든 final 필드나 @NonNull 필드에 대한 생성자가 자동으로 만들어지므로 코드의 양을 줄일 수 있습니다.
//    userService 클래스를 사용하기 위한 Lombok 어노테이션.
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;


    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    // 실제 회원가입이 실행되는 메소드
    public User join(String userName, String password) {
        // 회원 가입을 진행할 때 userName의 중복을 체크하고,
        // 중복된 userName이 있으면 에러를 처리한다. (2023-05-06)
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        // 그게 아니라면, UserEntity 객체를 생성하여 회원가입을 진행합니다. (2023-05-06)
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    // 실제 로그인이 실행되는 메소드
    // Spring Security를 이용해 JWT(JSON Web Token)를 사용.
    // => 로그인 시, 아이디 토큰[암호화된 문자열]을 발행해서 인증함.
    public String login(String userName, String password) {
        // (1) 회원가입 여부 체크.
        // => userEntityRepository에서 검색한 userName이 비어있는 경우, 해당 에러를 반환한다.
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // (2) 비밀번호 체크
        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성.
        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }

    public User loadUserByUserName(String userName) {
        // userName을 입력받아 해당 사용자 엔티티를 데이터베이스에서 찾아서, User 객체로 변환하여 반환합니다.
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                // 만약 해당 사용자가 존재하지 않으면 USER_NOT_FOUND 에러를 던집니다.
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }

    public Page<Alarm> alarmList(String userName, Pageable pageable) {
        // userName을 입력받아 해당 사용자의 알림 목록을 pageable에 맞게 조회하여 Alarm 객체로 변환한 뒤 Page<Alarm> 형태로 반환합니다.
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // 만약 해당 사용자가 존재하지 않으면 USER_NOT_FOUND 에러를 던집니다.
        return alarmEntityRepository.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
