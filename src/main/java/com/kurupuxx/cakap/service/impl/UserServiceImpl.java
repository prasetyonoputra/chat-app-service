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
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.response.GetDetailUserResponse;
import com.kurupuxx.cakap.response.UserResponse;
import com.kurupuxx.cakap.service.UserService;
import com.kurupuxx.cakap.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${dir.image.profile.upload}")
    private String uploadDir;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<GetDetailUserResponse> getDetailUser(String token) {
        GetDetailUserResponse response = new GetDetailUserResponse();
        response.setTimestamp(new Date());

        try {
            String username = jwtUtil.extractUsername(token);

            Optional<User> userOptional = userRepository.findByUsername(username);

            if (!userOptional.isPresent()) {
                response.setMessage("User not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOptional.get();
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
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            response.setMessage(e.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Override
    public byte[] getImageProfile(String token) {
        String username = jwtUtil.extractUsername(token);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                return Files.readAllBytes(Paths.get(user.getPathImageProfile()));
            } catch (IOException e) {
                System.out.println("Error reading image file: " + e.getMessage());
            }
        } else {
            System.out.println("User not found for username: " + username);
        }

        return null;
    }

}