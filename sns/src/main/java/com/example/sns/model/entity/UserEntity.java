package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "\"user\"")

@SQLDelete(sql = "UPDATE \"user\" SET removed_at = NOW() WHERE id=?")   // Delete 된 시간 자동 저장.
@Where(clause = "removed_at is NULL")   // 삭제 된 유저는 검색이 안되게 작성.

@NoArgsConstructor
// UserEntity 클래스 : DB에 저장할 때 사용하는 클래스
// => User과 UserEntity를 나눈 이유 : 비즈니스 로직과 데이터베이스 연동 로직을 분리하여 개발.
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @Column(name = "user_name", unique = true)
    private String userName;

    private String password;

    //  @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;  // 유저의 권한을 DB에 저장하는 필드. (관리자, 일반 유저)

    // Timestamp : 날짜와 시간 정보를 담는 데이터 타입.
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    // => 유저가 회원가입 한 시간을 저장하는 필드

    @Column(name = "updated_at")
    private Timestamp updatedAt;
    // 유저의 정보가 업데이트 된 시간을 저장하는 필드

    @Column(name = "removed_at")
    private Timestamp removedAt;
    // 유저의 정보가 삭제한 시간을 저장하는 필드 (만약 유저가 삭제를 안했는데, 삭제가 된 경우 => 로그를 찾기 쉬워짐.)

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    // userName과 encodedPwd로 UserEntity 객체를 생성하고, 이를 반환합니다.
    // 이러한 팩토리 메서드는 객체 생성 과정을 캡슐화하고, 객체 생성 시점에 유효성 검사 등을 수행할 수 있습니다.
    public static UserEntity of(String userName, String encodedPwd) {
        UserEntity entity = new UserEntity();
        entity.setUserName(userName);
        entity.setPassword(encodedPwd);
        return entity;
    }
}