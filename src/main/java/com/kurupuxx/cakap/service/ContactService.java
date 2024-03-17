package com.kurupuxx.cakap.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;

@Service
public interface ContactService {
    ResponseEntity<GetListContactResponse> getContacts(String token);
    ResponseEntity<CommonResponse> addContact(String token, String username);
}