package com.kurupuxx.cakap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.cakap.request.AuthenticationRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import com.kurupuxx.cakap.response.GetDetailUserResponse;
import com.kurupuxx.cakap.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestParam("imageProfile") MultipartFile imageProfile,
            @RequestParam("user") String userJson) {
        return userService.registerUser(imageProfile, userJson);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest request) {
        return userService.loginUser(request);
    }

    @GetMapping("/user")
    public ResponseEntity<GetDetailUserResponse> getDetailUser(@RequestParam("token") String token) {
        return userService.getDetailUser(token);
    }
}