package com.kurupuxx.chat.services;

import com.kurupuxx.chat.requests.LoginRequest;
import com.kurupuxx.chat.requests.RegisterRequest;
import com.kurupuxx.chat.response.AuthenticationResponse;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService extends UserDetailsService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest) throws Exception;

    AuthenticationResponse loginUser(LoginRequest request);

    Map<String, Boolean> checkToken(Map<String, String> request) throws Exception;
}