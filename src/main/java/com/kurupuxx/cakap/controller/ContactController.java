package com.kurupuxx.cakap.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListContactResponse;
import com.kurupuxx.cakap.service.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<GetListContactResponse> getListContact() {
        return contactService.getContacts();
    }

    @PostMapping
    public ResponseEntity<CommonResponse> addContact(@RequestBody Map<String, String> requestBody) {

        return contactService.addContact(requestBody.get("username"));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> deleteContact(@RequestParam("idUser") Long idUser) {
        return contactService.deleteContact(idUser);
    }
}
