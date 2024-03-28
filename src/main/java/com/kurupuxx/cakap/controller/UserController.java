package com.kurupuxx.cakap.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurupuxx.cakap.response.CommonResponse;
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

    @PostMapping("/set_socket_id")
    public ResponseEntity<CommonResponse> setSocketId(@RequestBody Map<String, String> requestBody) {
        return userService.setSocketId(requestBody.get("socketId"));
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
