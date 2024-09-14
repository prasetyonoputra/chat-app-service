package com.kurupuxx.chat.service.impl;

import com.kurupuxx.chat.model.User;
import com.kurupuxx.chat.repository.UserRepository;
import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.GetDetailUserResponse;
import com.kurupuxx.chat.response.UserResponse;
import com.kurupuxx.chat.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${dir.image.profile.upload}")
    private String uploadDir;

    @Override
    public ResponseEntity<GetDetailUserResponse> getDetailUser() {
        GetDetailUserResponse response = new GetDetailUserResponse();
        response.setTimestamp(new Date());

        User user = getCurrentUser();

        UserResponse userResponse = buildUserResponse(user);

        response.setMessage("Success get user!");
        response.setUser(userResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    public byte[] getImageProfile() {
        User user = getCurrentUser();
        try {
            return readImageFile(user.getPathImageProfile());
        } catch (IOException e) {
            System.out.println("Error reading image file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ResponseEntity<CommonResponse> setSocketId(String socketId) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        
        User user = getCurrentUser();
        user.setSocketId(socketId);
        user.setStatus("Online");
        userRepository.save(user);

        System.out.println(user.getUsername());
        System.out.println(socketId);

        response.setMessage("Success set socket id!");
        return ResponseEntity.ok(response);
    }

    // Helper method to retrieve the current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }

    // Helper method to build UserResponse from User entity
    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();
    }

    // Helper method to read image file bytes
    private byte[] readImageFile(String imagePath) throws IOException {
        return Files.readAllBytes(Path.of(imagePath));
    }
}
