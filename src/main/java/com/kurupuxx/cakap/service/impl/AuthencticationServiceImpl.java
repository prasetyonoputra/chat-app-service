package com.kurupuxx.cakap.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurupuxx.cakap.model.User;
import com.kurupuxx.cakap.repository.UserRepository;
import com.kurupuxx.cakap.request.AuthenticationRequest;
import com.kurupuxx.cakap.response.AuthenticationResponse;
import com.kurupuxx.cakap.service.AuthenticationService;
import com.kurupuxx.cakap.util.JwtUtil;

@Service
public class AuthencticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${dir.image.profile.upload}")
    private String uploadDir;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<AuthenticationResponse> registerUser(MultipartFile imageProfile, String userJson) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setTimestamp(new Date());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

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

            if (imageProfile != null && !imageProfile.isEmpty()) {
                @SuppressWarnings("null")
                String fileName = StringUtils.cleanPath(imageProfile.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageProfile.getInputStream(), filePath);
                user.setPathImageProfile(filePath.toString());
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            UserDetails userDetails = loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            authenticationResponse.setToken(token);
            authenticationResponse.setMessage("Success Registration!");

            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getLocalizedMessage());
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