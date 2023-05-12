package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // 자동으로 필드 getter/setter, equals, hashCode, toString 등의 메소드를 생성합니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 생성합니다.
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성합니다.

// Post 요청에서 Comment를 작성하기 위한 Request 객체입니다.
public class PostCommentRequest {
    private String comment;     // 댓글 내용을 저장하는 필드입니다.
}