package com.acnecare.api.post.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.post.Repository.CommentRepository;
import com.acnecare.api.post.Repository.PostsRepository;
import com.acnecare.api.post.dto.Request.CommentRequest;
import com.acnecare.api.post.dto.Response.CommentResponse;
import com.acnecare.api.post.entity.Comment;
import com.acnecare.api.post.entity.Posts;
import com.acnecare.api.post.mapper.CommentMapper;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    PostsRepository postsRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    public List<CommentResponse> getAllComments(String postId){
        if (!postsRepository.existsById(postId)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findByPostsIdOrderByCreateAtDesc(postId);

        return comments.stream()
                        .map(commentMapper::toCommentResponse)
                        .toList();
    }
    
    public CommentResponse createComment(String postId, CommentRequest request){
        String userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        Posts posts = postsRepository.findById(postId)
            .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND));
        Comment comment = Comment.builder()
            .commentContent(request.getCommentContent())
            .user(user)
            .posts(posts)
            .createAt(LocalDateTime.now())
            .build();
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public CommentResponse updateComment(String id, CommentRequest request){
        String userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        Comment comment = commentRepository.findById(id)
            .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        comment.setCommentContent(request.getCommentContent());
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public void deleteComment(String id){
        String userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        Comment comment = commentRepository.findById(id)
            .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        if(!comment.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        commentRepository.delete(comment);
    }
}
