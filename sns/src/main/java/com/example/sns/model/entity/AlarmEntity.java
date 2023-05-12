package com.example.sns.model.entity;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity     // JPA Entity임을 나타내는 어노테이션입니다.

// @Table: 알람 정보를 저장할 데이터베이스 테이블의 이름과 인덱스를 지정하는 어노테이션입니다.
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter

// @TypeDef: Hibernate에서 사용할 UserType을 등록하는 어노테이션입니다.
// 이 클래스에서는 jsonb라는 이름으로 JsonBinaryType 클래스를 등록합니다.
// JsonBinaryType은 JSON 형태의 데이터를 바이너리 형태로 저장하기 위한 Hibernate Type입니다.
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)

// @SQLDelete: 논리 삭제를 구현하기 위한 Hibernate 어노테이션입니다. deleted_at 필드에 현재 시간을 저장합니다.
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() where id=?")

// @Where: 논리 삭제를 구현하기 위한 Hibernate 어노테이션입니다. deleted_at 필드가 NULL인 경우만 조회합니다.
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne  // @ManyToOne, @JoinColumn: 다대일(N:1) 관계를 매핑하는 어노테이션입니다. 
    @JoinColumn(name = "user_id")       // user_id 필드를 FK로 지정합니다.
    // 알람을 받은 사람을 나타내는 필드
    private UserEntity user;

    @Enumerated(EnumType.STRING)        // Enum 타입 필드의 매핑을 지정하는 어노테이션입니다.
    private AlarmType alarmType;

    @Type(type = "jsonb")               // 이 필드가 Postgres JSONB 타입과 매핑되는 것을 명시합니다.
    @Column(columnDefinition = "json")  // 이 필드의 데이터베이스 컬럼이 JSON 타입인 것을 명시합니다.
    // 알람에 대한 추가적인 인자값을 담는 필드
    private AlarmArgs args;

    @Column(name = "registered_at")
    // 해당 알람이 등록된 시간을 나타내는 필드
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    // 해당 알람이 업데이트된 시간을 나타내는 필드
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    // 해당 알람이 삭제된 시간을 나타내는 필드
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        return entity;
    }
}