package com.kurupuxx.cakap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kurupuxx.cakap.response.GetDetailUserResponse;
import com.kurupuxx.cakap.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/detail")
    public ResponseEntity<GetDetailUserResponse> getDetailUser(HttpServletRequest request) {
        return userService.getDetailUser();
    }

    @GetMapping("/detail/username")
    public ResponseEntity<GetDetailUserResponse> getDetailUserByUsername(@RequestParam("username") String username) {
        return userService.getDetailUserByUsername(username);
    }

    @SuppressWarnings("null")
    @GetMapping("/image")
    public ResponseEntity<byte[]> kirimGambar() {        
        byte[] imageProfile = userService.getImageProfile();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageProfile);
    }
}
