package com.acnecare.api.post.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.entity.PostsImages;

@Mapper(componentModel = "spring")
public interface PostsImagesMapper {
    PostsImageResponse toPostsImageResponse(PostsImages postsImages);
    List<PostsImageResponse> toPostsImageResponseList(List<PostsImages> postsImagesList);
}
