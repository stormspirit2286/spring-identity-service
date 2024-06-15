package com.duynguyen.identityservice.service;

import com.duynguyen.identityservice.dto.request.UserCreationRequest;
import com.duynguyen.identityservice.dto.request.UserUpdateRequest;
import com.duynguyen.identityservice.dto.response.UserResponse;
import com.duynguyen.identityservice.entity.User;
import com.duynguyen.identityservice.exception.AppException;
import com.duynguyen.identityservice.exception.ErrorCode;
import com.duynguyen.identityservice.mapper.UserMapper;
import com.duynguyen.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class UserService {
    UserMapper userMapper;
    UserRepository userRepository;


    public UserResponse createUser(UserCreationRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User userCreated = userRepository.save(userMapper.toUser(request));
        UserResponse userResponse = userMapper.toUserResponse(userCreated);
        log.info("User created " + userCreated.getId());
        return userResponse;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(String userId) {
        userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.deleteById(userId);
        return "Deleted User";
    }
}
