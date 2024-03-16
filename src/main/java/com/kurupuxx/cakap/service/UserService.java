package com.kurupuxx.cakap.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.request.AuthenticationRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import com.kurupuxx.cakap.response.GetDetailUserResponse;

@Service
public interface UserService extends UserDetailsService {
    ResponseEntity<AuthenticationResponse> registerUser(User user);
    ResponseEntity<AuthenticationResponse> loginUser(AuthenticationRequest request);
    ResponseEntity<GetDetailUserResponse> getDetailUser(String token);
}