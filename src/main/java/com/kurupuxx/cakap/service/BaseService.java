package com.kurupuxx.cakap.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.util.JwtUtil;

@Service
public class BaseService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public User getUserByToken(String token) {
        String username = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            return null;
        }

        return userOptional.get();
    }
}
