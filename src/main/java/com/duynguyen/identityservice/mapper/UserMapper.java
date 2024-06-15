package com.duynguyen.identityservice.mapper;

import com.duynguyen.identityservice.dto.request.UserCreationRequest;
import com.duynguyen.identityservice.dto.request.UserUpdateRequest;
import com.duynguyen.identityservice.dto.response.UserResponse;
import com.duynguyen.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @Mapping(target = "id", source = "id")
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
