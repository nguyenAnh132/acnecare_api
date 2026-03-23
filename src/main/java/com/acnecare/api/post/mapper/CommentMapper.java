package com.acnecare.api.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.acnecare.api.post.dto.Response.CommentResponse;
import com.acnecare.api.post.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.avatarUrl", target = "avatarUrl")
    CommentResponse toCommentResponse(Comment comment);
}
