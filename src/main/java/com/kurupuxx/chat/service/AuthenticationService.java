package com.kurupuxx.chat.service;

import com.kurupuxx.chat.request.LoginRequest;
import com.kurupuxx.chat.request.RegisterRequest;
import com.kurupuxx.chat.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService extends UserDetailsService {
    ResponseEntity<AuthenticationResponse> registerUser(RegisterRequest registerRequest);

    ResponseEntity<AuthenticationResponse> loginUser(LoginRequest request);
}