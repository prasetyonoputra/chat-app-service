package com.kurupuxx.cakap.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetDetailUserResponse;

@Service
public interface UserService {
    ResponseEntity<GetDetailUserResponse> getDetailUser();
    ResponseEntity<CommonResponse> setSocketId(String socketId);
    byte[] getImageProfile();
}