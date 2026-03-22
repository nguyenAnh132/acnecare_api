package com.acnecare.api.post.mapper;

import org.mapstruct.Mapper;

import com.acnecare.api.post.dto.Response.CommentResponse;
import com.acnecare.api.post.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toCommentResponse(Comment comment);
}
