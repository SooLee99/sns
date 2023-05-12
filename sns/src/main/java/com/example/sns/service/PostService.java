package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.example.sns.model.Comment;
import com.example.sns.model.Post;
import com.example.sns.model.entity.*;
import com.example.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service                    // 이 클래스가 Spring에서 Service Bean으로 사용됨
@RequiredArgsConstructor    // 생성자를 자동으로 생성하고 클래스의 final 필드를 인자로 받는 생성자를 만듭니다.
// 이 클래스는 게시물(Post)에 대한 CRUD(Create, Read, Update, Delete) 및 관련된 작업을 처리합니다.
public class PostService {

    private final PostEntityRepository postEntityRepository;    // 게시글 JPA 엔티티 클래스
    private final UserEntityRepository userEntityRepository;    // 이용자 JPA 엔티티 클래스
    private final LikeEntityRepository likeEntityRepository;    // 좋아요 JPA 엔티티 클래스
    private final CommentEntityRepository commentEntityRepository;  // 댓글 JPA 엔티티 클래스
    private final AlarmEntityRepository alarmEntityRepository;      // 알람 JPA 엔티티 클래스

    // @Transactional: 이 애플리케이션에서는 JPA를 사용하므로,
    // 이 어노테이션을 사용하면 메소드 실행 전에 트랜잭션이 시작되고,
    // 메소드가 정상적으로 완료되면 트랜잭션이 커밋되고, 예외가 발생하면 롤백됩니다.
    @Transactional
    // 새로운 게시물을 생성하는 메소드
    // => 게시물 제목(title), 본문(body), 사용자 이름(userName)을 인자로 받습니다.
    public void create(String title, String body, String userName) {
        // UserEntity를 사용하여 게시물을 만듭니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // save() 메소드를 사용하여 생성된 게시물(PostEntity)을 저장합니다.
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    // 게시물을 수정하는 메소드
    // => 게시물 제목(title), 본문(body), 사용자 이름(userName), 게시물 ID(postId)를 인자로 받습니다.
    public Post modify(String title, String body, String userName, Integer postId) {
        // UserEntity를 사용하여 게시물의 작성자를 가져옵니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // 사용자가 게시물 수정 권한이 있는지 확인합니다.
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        // setTitle(), setBody() 메소드를 사용하여 제목과 본문을 수정합니다.
        postEntity.setTitle(title);
        postEntity.setBody(body);

        // saveAndFlush() 메소드를 사용하여 수정된 게시물(PostEntity)을 반환하여 저장합니다.
        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    // 게시물을 삭제하는 메소드
    // => 사용자 이름(userName)과 게시물 ID(postId)를 인자로 받습니다.
    public void delete(String userName, Integer postId) {
        // UserEntity를 사용하여 게시물의 작성자를 가져옵니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // 사용자가 게시물 삭제 권한이 있는지 확인합니다.
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        // delete() 메소드를 사용하여 게시물(PostEntity)을 삭제합니다.
        postEntityRepository.delete(postEntity);
    }

    // 모든 게시물을 가져오는 메소드
    // => 모든 게시물을 가져옵니다. pageable을 사용하여 페이징 처리를 합니다.
    public Page<Post> list(Pageable pageable) {
        // findAll() 메소드를 사용하여 게시물(PostEntity) 리스트를 가져온 후, map() 메소드를 사용하여 Post 객체로 변환합니다.
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    // 사용자의 게시물만 가져오는 메소드
    // => 사용자 이름(userName)과 pageable을 인자로 받습니다.
    public Page<Post> my(String userName, Pageable pageable) {
        // UserEntity를 사용하여 사용자를 가져옵니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // findAllByUser() 메소드를 사용하여 사용자가 작성한 게시물(PostEntity) 리스트를 가져온 후,
        // map() 메소드를 사용하여 Post 객체로 변환합니다.
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }


    @Transactional
    // 게시물에 좋아요를 추가하는 메소드
    // => 게시물 ID(postId)와 사용자 이름(userName)를 인자로 받습니다.
    public void like(Integer postId, String userName) {

        // UserEntity를 사용하여 게시물의 작성자를 가져옵니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // 해당 게시물이 존재하는지 확인하고, 사용자가 이미 좋아요를 눌렀는지 체크합니다.
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            // 이미 좋아요를 눌렀다면 예외를 발생시킵니다.
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
        });

        // 좋아요가 되어 있지 않다면, LikeEntity 객체를 생성하여 저장합니다.
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

        // 해당 게시물 작성자에게 알람을 보냅니다.
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    // 게시물의 좋아요 개수를 반환하는 메소드
    // => 게시물 ID를 인자로 받습니다.
    public int likeCount(Integer postId) {

        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // 해당 게시물이 존재하는지 확인하고, likeEntityRepository에서 해당 게시물의 좋아요 개수를 조회하여 반환합니다.
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    // 게시물에 댓글을 추가하는 메소드
    // => 게시물 ID, 사용자 이름, 댓글 내용을 인자로 받습니다.
    public void comment(Integer postId, String userName, String comment) {
        // UserEntity를 사용하여 게시물의 작성자를 가져옵니다.
        UserEntity userEntity = getUserEntityOrException(userName);

        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // 해당 게시물이 존재하는지 확인하고, 댓글을 CommentEntity 객체로 생성하여 저장합니다.
        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        // 해당 게시물 작성자에게 알람을 보냅니다.
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    // 게시물에 작성된 댓글 목록을 조회하는 메소드
    // => 게시물 ID와 Pageable 객체를 인자로 받습니다.
    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        // PostEntity를 사용하여 게시물을 가져옵니다.
        PostEntity postEntity = getPostEntityOrException(postId);

        // commentEntityRepository에서 해당 게시물의 댓글을 페이지네이션하여 조회하여 반환합니다.
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }


    // 게시물 ID를 인자로 받아서, 해당 게시물이 존재하는지 확인하고, 존재하지 않으면 예외를 발생시킵니다.
    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    // 사용자 이름을 인자로 받아서, 해당 사용자가 존재하는지 확인하고, 존재하지 않으면 예외를 발생시킵니다.
    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}