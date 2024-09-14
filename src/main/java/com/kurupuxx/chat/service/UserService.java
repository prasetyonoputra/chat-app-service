package com.kurupuxx.chat.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.GetDetailUserResponse;

@Service
public interface UserService {
    ResponseEntity<GetDetailUserResponse> getDetailUser();
    ResponseEntity<CommonResponse> setSocketId(String socketId);
    byte[] getImageProfile();
}