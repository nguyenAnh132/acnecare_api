package com.acnecare.api.post.controller;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.post.dto.Request.PostsRequest;
import com.acnecare.api.post.dto.Response.PostsResponse;
import com.acnecare.api.post.service.PostsService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class PostsController {
    
    PostsService postsService;
    SimpMessagingTemplate messagingTemplate;

    @GetMapping
    ApiResponse<List<PostsResponse>> getAllPosts() {
        List<PostsResponse> response = postsService.getAllPosts();

        return ApiResponse.<List<PostsResponse>>builder()
            .code(1000)
            .message("Posts have been retrieved successfully")
            .result(response)
            .build();
    }

    @GetMapping("/users/{userId}")
    ApiResponse<List<PostsResponse>> getPostsByUserId(@PathVariable String userId) {
        List<PostsResponse> response = postsService.getPostsByUserId(userId);

        return ApiResponse.<List<PostsResponse>>builder()
            .code(1000)
            .message("Posts have been retrieved successfully")
            .result(response)
            .build();
    }

    @GetMapping("/{postId}")
    ApiResponse<PostsResponse> getPostById(@PathVariable String postId) {
        PostsResponse response = postsService.getPostById(postId);

        return ApiResponse.<PostsResponse>builder()
            .code(1000)
            .message("Post has been retrieved successfully")
            .result(response)
            .build();
    }
    
    @PostMapping("/{userId}")
    ApiResponse<PostsResponse> createPost(@PathVariable String userId, @Valid @RequestBody PostsRequest request) {
        PostsResponse response = postsService.createPost(userId, request);

        return ApiResponse.<PostsResponse>builder()
            .code(1000)
            .message("Post has been created successfully")
            .result(response)
            .build();
    }
    // Chú ý: Cần thêm userId vào request body để xác định người dùng nào đang cập nhật bài viết, hoặc có thể lấy userId từ token nếu có authentication
    // Cập nhật bài viết
    @PutMapping("/update/{userId}/{postId}")
    ApiResponse<PostsResponse> updatePost(@PathVariable String userId, @PathVariable String postId, @Valid @RequestBody PostsRequest request) {
        PostsResponse response = postsService.updatePost(userId, postId, request);

        messagingTemplate.convertAndSend("/topic/posts/update", response);

        return ApiResponse.<PostsResponse>builder()
            .code(1000)
            .message("Post has been updated successfully")
            .result(response)
            .build();
    }
    // Xóa bài viết
    @DeleteMapping("/delete/{userId}/{postId}")
    ApiResponse<Void> deletePost(@PathVariable String userId, @PathVariable String postId) {
        postsService.deletePost(userId, postId);

        messagingTemplate.convertAndSend("/topic/posts/delete", postId);

        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Post has been deleted successfully")
            .build();
    }

}
