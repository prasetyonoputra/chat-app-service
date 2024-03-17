package com.kurupuxx.cakap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.cakap.request.AuthenticationRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import com.kurupuxx.cakap.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestParam("imageProfile") MultipartFile imageProfile,
            @RequestParam("user") String userJson) {
        return authenticationService.registerUser(imageProfile, userJson);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest request) {
        return authenticationService.loginUser(request);
    }
}