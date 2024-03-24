package com.kurupuxx.cakap.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GetDetailUserResponse> getDetailUserByUsername(String username) {
        GetDetailUserResponse response = new GetDetailUserResponse();
        response.setTimestamp(new Date());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByUsername(authentication.getName());

        if (userOptional.isEmpty()) {
            response.setMessage("Login First!");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


        User userToGet = userRepository.findByUsername(username).get();

        UserResponse userResponse = UserResponse.builder()
                .email(userToGet.getEmail())
                .firstName(userToGet.getFirstName())
                .lastName(userToGet.getLastName())
                .username(userToGet.getUsername())
                .status(userToGet.getStatus())
                .socketId(userToGet.getSocketId())
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