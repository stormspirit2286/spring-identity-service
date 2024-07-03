package com.duynguyen.identityservice.controller;

import com.duynguyen.identityservice.dto.request.UserCreationRequest;
import com.duynguyen.identityservice.dto.request.UserUpdateRequest;
import com.duynguyen.identityservice.dto.response.ApiResponse;
import com.duynguyen.identityservice.dto.response.UserResponse;
import com.duynguyen.identityservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping()
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest user) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(user))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().stream().forEach(it -> log.info(it.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUserById(@PathVariable("userId") String userId) {
        return ApiResponse.<String>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        log.info("Current user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }
}
