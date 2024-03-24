package com.kurupuxx.cakap.service;

import com.kurupuxx.cakap.response.GetDetailUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;

@Service
public interface ContactService {
    ResponseEntity<GetListContactResponse> getContacts();

    ResponseEntity<CommonResponse> addContact(String username);

    ResponseEntity<GetDetailUserResponse> getDetailContact(String username);

    ResponseEntity<CommonResponse> deleteContact(String idUser);
}
