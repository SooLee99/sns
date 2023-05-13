package com.example.sns.controller.request;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter             // 자동으로 필드 getter 메소드를 생성합니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 생성합니다.

// 게시물 작성을 위한 Request 객체입니다.
public class PostWriteRequest {

    private String title;   // 게시물의 제목을 저장하는 필드입니다.
    private String body;    // 게시물의 내용을 저장하는 필드입니다.
}