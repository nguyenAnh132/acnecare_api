package com.acnecare.api.post.dto.Request;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentRequest {
    // Thêm valid vào đây để bắt lỗi và ràng buộc
    @Size(min = 1, max = 100, message = "INVALID_COMMENT_CONTENT")
    String commentContent;
}
