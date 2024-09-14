package com.kurupuxx.chat.controller;

import com.kurupuxx.chat.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.chat.request.LoginRequest;
import com.kurupuxx.chat.response.AuthenticationResponse;
import com.kurupuxx.chat.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestParam(required = false) MultipartFile imageProfile,
                                                               @RequestParam String firstName,
                                                               @RequestParam String lastName,
                                                               @RequestParam String email,
                                                               @RequestParam String username,
                                                               @RequestParam String password) {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .imageProfile(imageProfile)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(username)
                .password(password)
                .build();

        return authenticationService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginRequest request) {
        return authenticationService.loginUser(request);
    }
}