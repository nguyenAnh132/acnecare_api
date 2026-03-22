package com.acnecare.api.post.dto.Request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class PostsRequest {
    @Size(min = 1, max = 100, message = "INVALID_POST_CONTENT")
    String postContent;
    @Size(min = 1, max = 100, message = "INVALID_POST_TITLE")   
    String postTitle;
    @Pattern(regexp = "^(ACTIVE|BLOCK)$", message = "INVALID_POST_STATUS")
    String status;
}
