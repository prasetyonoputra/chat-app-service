package com.kurupuxx.cakap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurupuxx.cakap.response.GetDetailUserResponse;
import com.kurupuxx.cakap.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/detail")
    public ResponseEntity<GetDetailUserResponse> getDetailUser(HttpServletRequest request) {
        return userService.getDetailUser();
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
