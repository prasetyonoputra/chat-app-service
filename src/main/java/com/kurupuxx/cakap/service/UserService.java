package com.kurupuxx.cakap.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.AuthenticationRequest;
import com.kurupuxx.cakap.model.AuthenticationResponse;
import com.kurupuxx.cakap.model.User;

@Service
public interface UserService extends UserDetailsService {
    ResponseEntity<AuthenticationResponse> registerUser(User user);
    ResponseEntity<AuthenticationResponse> loginUser(AuthenticationRequest request);
}