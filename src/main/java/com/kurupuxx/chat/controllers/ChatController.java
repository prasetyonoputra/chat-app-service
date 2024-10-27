package com.kurupuxx.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kurupuxx.chat.requests.ChatRequest;
import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.services.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    ChatService chatService;

    @GetMapping
    public ResponseEntity<?> getListChat(@RequestParam String usernameReceiver) {
        return chatService.getListChat(usernameReceiver);
    }

    @PostMapping
    public ResponseEntity<CommonResponse> sendChat(@RequestParam String usernameReceiver, @RequestParam String message,
            @RequestParam(required = false) MultipartFile file) {

        ChatRequest chatRequest = ChatRequest.builder().usernameReceiver(usernameReceiver).message(message).file(file)
                .build();

        return chatService.sendMessage(chatRequest);
    }

    @PutMapping
    public ResponseEntity<?> editChat() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteChat() {
        return null;
    }
}
