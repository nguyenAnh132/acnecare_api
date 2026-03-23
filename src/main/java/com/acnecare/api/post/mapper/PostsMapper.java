package com.acnecare.api.post.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.acnecare.api.post.dto.Request.PostsRequest;
import com.acnecare.api.post.dto.Response.PostsResponse;
import com.acnecare.api.post.entity.Posts;
import com.acnecare.api.role.dto.response.RoleResponse;
import com.acnecare.api.role.entity.Role;

@Mapper(componentModel = "spring")
public interface PostsMapper {
    Posts toPosts(PostsRequest request);    
    PostsResponse toPostsResponse(Posts posts);
    
    @Mapping(target = "user", ignore = true)
    List<PostsResponse> toPostsResponseList(List<Posts> postsList);
    Set<RoleResponse> toRoleResponseSet(Set<Role> roles);
}
