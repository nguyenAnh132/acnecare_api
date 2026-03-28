package com.acnecare.api.post.controller;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    SimpMessagingTemplate messagingTemplate;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<List<PostsImageResponse>> uploadPostImages(
            @PathVariable String postId,
            @RequestParam("files") List<MultipartFile> files) {
        
        List<PostsImageResponse> uploadedImages = postImageService.uploadAndSavePostImages(postId, files);
        
        messagingTemplate.convertAndSend("/topic/posts/" + postId + "/images/upload", uploadedImages);

        return ApiResponse.<List<PostsImageResponse>>builder()
                .code(1000)
                .message("Upload image has been successfully")
                .result(uploadedImages)
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

    @DeleteMapping("/{imageId}")
    public ApiResponse<Void> deletePostImage(@PathVariable("postId") String postId, @PathVariable String imageId) {
        
        postImageService.deletePostImage(imageId);
        
        messagingTemplate.convertAndSend("/topic/posts/" + postId + "/images/delete", imageId);
        
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Image has been deleted successfully")
                .build();
    }
}
