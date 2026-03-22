package com.acnecare.api.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.service.PostImageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/posts/{postId}/images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostsImageController {
    PostImageService postImageService;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<List<PostsImageResponse>> uploadPostImages(
            @PathVariable String postId,
            @RequestParam("files") List<MultipartFile> files) {
        
        return ApiResponse.<List<PostsImageResponse>>builder()
                .code(1000)
                .message("Upload image has been successfully")
                .result(postImageService.uploadAndSavePostImages(postId, files))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PostsImageResponse>> getImagesByPost(@PathVariable String postId) {
        return ApiResponse.<List<PostsImageResponse>>builder()
                .code(1000)
                .message("List of successfully retrieved images")
                .result(postImageService.getImagesByPostId(postId))
                .build();
    }
}
