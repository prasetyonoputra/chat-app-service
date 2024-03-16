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

import com.kurupuxx.cakap.model.User;
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
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
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