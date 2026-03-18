package com.acnecare.api.post.mapper;

import org.mapstruct.Mapper;

import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.entity.PostsImages;

@Mapper(componentModel = "spring")
public interface PostsImagesMapper {
    PostsImageResponse toPostsImageResponse(PostsImages postsImages);
}
