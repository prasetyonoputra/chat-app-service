package com.kurupuxx.cakap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.model.AuthenticationRequest;
import com.kurupuxx.cakap.model.AuthenticationResponse;
import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.service.UserService;
import com.kurupuxx.cakap.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<AuthenticationResponse> registerUser(User user) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setTimestamp(new Date());

        try {
            Optional<User> findUserByEmail = userRepository.findByEmail(user.getEmail());
            if (findUserByEmail.isPresent()) {
                authenticationResponse.setMessage("Email already exists!");
                return ResponseEntity.badRequest().body(authenticationResponse);
            }

            Optional<User> findUserByUsername = userRepository.findByUsername(user.getUsername());
            if (findUserByUsername.isPresent()) {
                authenticationResponse.setMessage("Username already exists!");
                return ResponseEntity.badRequest().body(authenticationResponse);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            UserDetails userDetails = loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            authenticationResponse.setToken(token);
            authenticationResponse.setMessage("Success Registration!");

            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception e) {
            authenticationResponse.setMessage(e.getLocalizedMessage());

            return ResponseEntity.internalServerError().body(authenticationResponse);
        }
    }

    @Override
    public ResponseEntity<AuthenticationResponse> loginUser(AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setTimestamp(new Date());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            authenticationResponse.setMessage("Invalid username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }

        UserDetails userDetails = loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        authenticationResponse.setToken(token);
        authenticationResponse.setMessage("Success Authentication!");

        return ResponseEntity.ok(authenticationResponse);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());
    }

}