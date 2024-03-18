package com.kurupuxx.cakap.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.response.GetDetailUserResponse;
import com.kurupuxx.cakap.response.UserResponse;
import com.kurupuxx.cakap.service.UserService;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();

        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();

        response.setMessage("Success get user!");
        response.setUser(userResponse);

        return ResponseEntity.ok(response);
    }

    @Override
    public byte[] getImageProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        try {
            return Files.readAllBytes(Paths.get(user.getPathImageProfile()));
        } catch (IOException e) {
            System.out.println("Error reading image file: " + e.getMessage());
        }

        return null;
    }

}