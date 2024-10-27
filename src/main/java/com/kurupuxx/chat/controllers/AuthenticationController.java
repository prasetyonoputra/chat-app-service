package com.kurupuxx.chat.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.chat.requests.LoginRequest;
import com.kurupuxx.chat.requests.RegisterRequest;
import com.kurupuxx.chat.response.AuthenticationResponse;
import com.kurupuxx.chat.services.AuthenticationService;
import com.kurupuxx.chat.utils.BaseAppController;
import com.kurupuxx.chat.utils.BaseResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends BaseAppController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> registerUser(
            @RequestParam(required = false, name = "image_profile") MultipartFile imageProfile,
            @RequestParam(name = "first_name") String firstName, @RequestParam(name = "last_name") String lastName,
            @RequestParam(name = "email") String email, @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {

        RegisterRequest registerRequest = RegisterRequest.builder().imageProfile(imageProfile).firstName(firstName)
                .lastName(lastName).email(email).username(username).password(password).build();

        try {
            return toResponse(authenticationService.registerUser(registerRequest), "Registration successfully!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> loginUser(@RequestBody LoginRequest request) {
        try {
            return toResponse(authenticationService.loginUser(request), "Login successfully!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @PostMapping("/check-token")
    public ResponseEntity<BaseResponse<String>> checkToken(@RequestBody Map<String, String> request) {
        try {
            return toResponse("", "Login successfully!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }
}