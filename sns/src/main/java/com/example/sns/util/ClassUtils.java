package com.example.sns.util;

public class ClassUtils {

    public static <T> T getSafeCastInstance(Object o, Class<T> clazz) {
        // 만약 o가 clazz의 인스턴스가 아니라면 빈 Optional을 반환합니다.
        // 그렇지 않다면 o를 clazz의 인스턴스로 안전하게 캐스팅한 결과를 Optional에 담아 반환합니다.
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }
}