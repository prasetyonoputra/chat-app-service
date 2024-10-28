package com.kurupuxx.chat.services;

import com.kurupuxx.chat.response.GetDetailUserResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.ContactUserResponse;

@Service
public interface ContactService {
    List<ContactUserResponse> getContacts() throws Exception;
    
    Map<String, Object> addContact(String username) throws Exception;

    ResponseEntity<GetDetailUserResponse> getDetailContact(String username);

    ResponseEntity<CommonResponse> deleteContact(String idUser);
}
