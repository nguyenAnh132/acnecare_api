package com.acnecare.api.post.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.post.Repository.LikesRepository;
import com.acnecare.api.post.Repository.PostsRepository;
import com.acnecare.api.post.entity.Likes;
import com.acnecare.api.post.entity.Posts;
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
public class LikesService {
    LikesRepository likesRepository;
    PostsRepository postsRepository;
    UserRepository userRepository;

    public boolean toggleLike(String postId){

        var userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
    
        Posts post = postsRepository.findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        boolean alreadyLiked = likesRepository.existsByPostsIdAndUserId(postId, userId);

        if (alreadyLiked) {
            Likes like = likesRepository.findByPostsIdAndUserId(postId, userId)
            .orElseThrow(() -> new AppException(ErrorCode.LIKE_NOT_FOUND));
            likesRepository.delete(like);
            return false;
        }

        Likes like = Likes.builder()
            .createAt(LocalDateTime.now())
            .user(user)
            .posts(post)
            .build();
        likesRepository.save(like);
        return true;
    }

    // public void deleteLikePost(String postId){
    //     var userId = CurrentUserId.getCurrentUserId()
    //         .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    //     Likes like = likesRepository.findByPostsIdAndUserId(postId, userId)
    //         .orElseThrow(() -> new AppException(ErrorCode.LIKE_NOT_FOUND));
    //     likesRepository.delete(like);
    // }

    // Đếm tổng số lượng Like của bài viết
    public long countLikesByPostId(String postId){
        return likesRepository.countByPostsId(postId);
    }
}
