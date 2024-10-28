package com.kurupuxx.chat.controllers;

import java.util.Map;

import com.kurupuxx.chat.response.GetDetailUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.services.ContactService;
import com.kurupuxx.chat.utils.BaseAppController;
import com.kurupuxx.chat.utils.BaseResponse;

@RestController
@RequestMapping("/api/contact")
public class ContactController extends BaseAppController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<BaseResponse> getListContact() {
        try {
            return toResponse(contactService.getContacts(), "Success get contact!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addContact(@RequestBody Map<String, String> requestBody) {
        try {
            return toResponse(contactService.addContact(requestBody.get("username")), "Successfully added contact!");
        } catch (Exception e) {
            return toResponse(e);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<GetDetailUserResponse> getDetailContact(@RequestParam String username) {
        return contactService.getDetailContact(username);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> deleteContact(@RequestParam String username) {
        return contactService.deleteContact(username);
    }
}
