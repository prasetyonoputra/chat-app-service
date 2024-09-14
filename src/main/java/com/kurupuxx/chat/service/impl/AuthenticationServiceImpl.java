package com.kurupuxx.chat.service.impl;

import com.kurupuxx.chat.model.User;
import com.kurupuxx.chat.repository.UserRepository;
import com.kurupuxx.chat.request.LoginRequest;
import com.kurupuxx.chat.request.RegisterRequest;
import com.kurupuxx.chat.response.AuthenticationResponse;
import com.kurupuxx.chat.service.AuthenticationService;
import com.kurupuxx.chat.util.JwtUtil;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${dir.image.profile.upload}")
    private String uploadDir;

    @Override
    public ResponseEntity<AuthenticationResponse> registerUser(RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setTimestamp(new Date());

        try {
            Optional<User> findUserByEmail = userRepository.findByEmail(registerRequest.getEmail());
            if (findUserByEmail.isPresent()) {
                authenticationResponse.setMessage("Email already exists!");
                return ResponseEntity.badRequest().body(authenticationResponse);
            }

            Optional<User> findUserByUsername = userRepository.findByUsername(registerRequest.getUsername());
            if (findUserByUsername.isPresent()) {
                authenticationResponse.setMessage("Username already exists!");
                return ResponseEntity.badRequest().body(authenticationResponse);
            }

            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .username(registerRequest.getUsername())
                    .password(registerRequest.getPassword())
                    .build();

            if (registerRequest.getImageProfile() != null) {
                String fileName = new Date().getTime() + "_"
                        + StringUtils.cleanPath(Objects.requireNonNull(registerRequest.getImageProfile().getOriginalFilename()));
                Path uploadPath = Path.of(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(registerRequest.getImageProfile().getInputStream(), filePath);
                user.setPathImageProfile(filePath.toString());
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setUpdatedBy("SYSTEM");
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
    public ResponseEntity<AuthenticationResponse> loginUser(LoginRequest request) {
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
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());
    }
}
