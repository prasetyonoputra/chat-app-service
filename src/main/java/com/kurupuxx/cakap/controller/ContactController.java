package com.kurupuxx.cakap.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;
import com.kurupuxx.cakap.service.ContactService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<GetListContactResponse> getListContact(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);

        return contactService.getContacts(token);
    }

    @PostMapping
    public ResponseEntity<CommonResponse> addContact(HttpServletRequest request,
            @RequestBody Map<String, String> requestBody) {
        String token = request.getHeader("Authorization").substring(7);

        return contactService.addContact(token, requestBody.get("username"));
    }
}
