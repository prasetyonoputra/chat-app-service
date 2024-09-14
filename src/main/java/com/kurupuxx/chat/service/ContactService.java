package com.kurupuxx.chat.service;

import com.kurupuxx.chat.response.GetDetailUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.GetListContactResponse;

@Service
public interface ContactService {
    ResponseEntity<GetListContactResponse> getContacts();

    ResponseEntity<CommonResponse> addContact(String username);

    ResponseEntity<GetDetailUserResponse> getDetailContact(String username);

    ResponseEntity<CommonResponse> deleteContact(String idUser);
}
