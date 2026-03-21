package com.acnecare.api.user.mapper;

import org.mapstruct.Mapper;
import com.acnecare.api.user.dto.request.UserCreationRequest;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.dto.response.UserResponse;
import org.mapstruct.Mapping;
import java.util.List;
import com.acnecare.api.user.dto.request.UserUpdateRequest;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(UserUpdateRequest request, @MappingTarget User user);

    UserResponse toUserCreationResponse(User user);

    List<UserResponse> toUserCreationResponses(List<User> users);

    @Mapping(target = "username", source = "email")
    @Mapping(target = "role", expression = "java(user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.joining(\", \")))")
    List<UserResponse> toUserResponseList(List<User> users);
}
