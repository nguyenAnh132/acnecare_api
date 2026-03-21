package com.acnecare.api.post.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.post.service.LikesService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LikeController {
    LikesService likesService;
    @PostMapping
    public ApiResponse<Void> likePost(@PathVariable String postId){
        likesService.toggleLike(postId);

        return ApiResponse.<Void> builder()
            .code(1000)
            .message("Liked the post")
            .build();
    }
}
