package com.acnecare.api.post.dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostsResponse {
    String id;
    String postContent;
    String postTitle;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserPostsResponse user;
    List<PostsImageResponse> postsImage;

    long likesCount;
    boolean isLiked;
    long commentsCount;
    List<CommentResponse> comments;
}