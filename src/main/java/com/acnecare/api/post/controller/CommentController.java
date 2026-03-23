package com.acnecare.api.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.post.dto.Request.CommentRequest;
import com.acnecare.api.post.dto.Response.CommentResponse;
import com.acnecare.api.post.service.CommentService;

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
public class CommentController {
    CommentService commentService;

    @GetMapping("/{postId}/comments")
    ApiResponse<List<CommentResponse>> getAllComment(@PathVariable String postId){
        List<CommentResponse> commentResponses = commentService.getAllComments(postId);
        return ApiResponse.<List<CommentResponse>>builder()
                .code(1000)
                .message("Retrieving all comments was successful.")
                .result(commentResponses)
                .build();
    }

    @PostMapping("/{postId}/comments")
    ApiResponse<CommentResponse> createComment(@PathVariable String postId, @Valid @RequestBody CommentRequest request){
        CommentResponse commentResponse = commentService.createComment(postId, request);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Comment has been successful.")
                .result(commentResponse)
                .build();
    }

    @PutMapping("/comments/{commentId}")
    ApiResponse<CommentResponse> updateComment(@PathVariable String commentId, @Valid @RequestBody CommentRequest request){
        CommentResponse commentResponse = commentService.updateComment(commentId, request);
        return ApiResponse.<CommentResponse>builder()
            .code(1000)
            .message("Comment has been successful updated.")
            .result(commentResponse)
            .build();
    }

    @DeleteMapping("/comments/{commentId}")
    ApiResponse<Void> deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Comment has been successful deleted")
            .build();
    }
}
