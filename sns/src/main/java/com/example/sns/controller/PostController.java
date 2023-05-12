package com.example.sns.controller;

import com.example.sns.controller.request.PostCommentRequest;
import com.example.sns.controller.request.PostCreateRequest;
import com.example.sns.controller.request.PostModifyRequest;
import com.example.sns.controller.response.CommentResponse;
import com.example.sns.controller.response.PostResponse;
import com.example.sns.controller.response.Response;
import com.example.sns.model.Post;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController                     // REST API를 처리하는 컨트롤러임을 나타낸다.
@RequestMapping("/api/v1/posts")    // (/api/v1/posts) 경로로 들어오는 HTTP 요청을 해당 컨트롤러에서 처리한다는 것을 명시한다.
@RequiredArgsConstructor            // 클래스 내부에 final 필드를 가진 생성자를 자동으로 생성해준다.

// PostController Class: 게시물(Post) 관련 API를 처리하는 컨트롤러 클래스.
public class PostController {

    // PostService 타입의 postService 필드를 선언하고, @RequiredArgsConstructor로 생성자를 만들어 자동으로 해당 필드를 주입받는다.
    private final PostService postService;

    @PostMapping
    // 게시글 작성 기능을 수행하는 메소드: postService의 create() 메소드를 호출하여 새로운 게시글을 작성한다.
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    // 게시글 수정 기능을 수행하는 메소드: postService의 modify() 메소드를 호출하여 postId에 해당하는 게시글을 수정한다.
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    // 게시글 삭제 기능을 수행하는 메소드: postService의 delete() 메소드를 호출하여 postId에 해당하는 게시글을 삭제한다.
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping
    // 전체 게시글 목록을 조회하는 메소드: postService의 list() 메소드를 호출하여 페이지네이션된 전체 게시글 목록을 조회한다.
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    // 사용자가 작성한 게시글 목록을 조회하는 메소드: postService의 my() 메소드를 호출하여 페이지네이션된 사용자가 작성한 게시글 목록을 조회한다.
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    // 게시글의 좋아요 기능을 하는 메소드: postService의 like() 메소드를 호출하여 postId에 해당하는 게시글을 좋아요 한다.
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    // 게시글 좋아요 개수를 조회하는 메소드: postService의 likeCount() 메소드를 호출하여 postId에 해당하는 게시글의 좋아요 개수를 조회한다.
    public Response<Integer> likeCount(@PathVariable Integer postId, Authentication authentication) {
        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    // 게시글 댓글 작성하는 메소드: postService의 comment() 메소드를 호출하여 postId에 해당하는 게시글에 댓글을 작성한다.
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    // 게시글 댓글 목록을 조회하는 메소드: postService의 getComments() 메소드를 호출하여 페이지네이션된 postId에 해당하는 게시글의 댓글 목록을 조회한다.
    public Response<Page<CommentResponse>> comment(@PathVariable Integer postId, Pageable pageable, Authentication authentication) {
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }

}