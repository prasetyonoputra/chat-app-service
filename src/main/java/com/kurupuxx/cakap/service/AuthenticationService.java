package com.kurupuxx.cakap.service;

import com.kurupuxx.cakap.request.LoginRequest;
import com.kurupuxx.cakap.request.RegisterRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService extends UserDetailsService {
    ResponseEntity<AuthenticationResponse> registerUser(RegisterRequest registerRequest);

    ResponseEntity<AuthenticationResponse> loginUser(LoginRequest request);
}