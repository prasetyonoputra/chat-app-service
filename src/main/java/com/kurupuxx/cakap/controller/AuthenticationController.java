package com.kurupuxx.cakap.controller;

import com.kurupuxx.cakap.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.cakap.request.LoginRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import com.kurupuxx.cakap.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestParam(name = "imageProfile", required = false) MultipartFile imageProfile,
                                                               @RequestParam("firstName") String firstName,
                                                               @RequestParam("lastName") String lastName,
                                                               @RequestParam("email") String email,
                                                               @RequestParam("username") String username,
                                                               @RequestParam("password") String password) {

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