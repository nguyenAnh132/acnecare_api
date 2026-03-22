package com.acnecare.api.post.dto.Response;

import java.time.LocalDateTime;

import com.acnecare.api.user.dto.response.UserResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String commentContent;
    LocalDateTime createAt;
}
