package com.example.sns.service;

import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 회원가입이_정상적으로_동작하는_경우(){
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        // 회원가입이 정상적으로 동작하는 경우
        // (1) UserEntityRepository 인터페이스에서 findByUserName() 메소드를 실행 시, 현재 입력된 userName 값이 없어야 합니다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // (2) UserEntityRepository 인터페이스의 save() 메서드가 호출이 될 때,
        //     UserEntity 클래스의 mock 객체를 생성하여 Optional로 감싼 결과를 반환합니다.
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(()-> userService.join(userName, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우(){
        String userName = "userName";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        // 회원가입 시, userName으로 이미 가입된 사용자가 있는 경우에 대한 테스트입니다.
        // (1) UserEntityRepository 인터페이스에서 findByUserName() 메서드를 호출할 때,
        // 이미 userName 값으로 가입된 사용자가 존재하는 것으로 설정하고, 값이 반환됩니다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // (2) UserEntityRepository 인터페이스에서 save() 메서드를 호출할 때, 영향을 주지 않습니다.
        //      save() 메서드가 호출되어도 Optional.of(mock(UserEntity.class))로 설정된 값이 반환됩니다.
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        // (3) join() 메서드를 호출할 때, userName 값이 이미 존재하는 경우
        //     SnsApplicationException 예외가 발생하도록 합니다.
        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        Assertions.assertDoesNotThrow(()-> userService.login(userName, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는_경우(){
        String userName = "userName";
        String password = "password";

        // 로그인 시, userName으로 가입된 사용자가 아닌 경우를 테스트합니다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // SnsApplicationException 예외가 발생하도록 합니다.
        Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
    }
    @Test
    void 로그인시_패스워드가_틀린_경우(){
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        // 로그인 시, userName으로 가입된 사용자가 아닌 경우를 테스트합니다.
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // SnsApplicationException 예외가 발생하도록 합니다.
        Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));
    }
}
