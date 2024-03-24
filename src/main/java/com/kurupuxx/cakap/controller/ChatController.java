package com.kurupuxx.cakap.controller;

import com.kurupuxx.cakap.model.Chat;
import com.kurupuxx.cakap.request.ChatRequest;
import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @GetMapping
    public ResponseEntity<?> getListChat(@RequestParam("usernameReceiver") String usernameReceiver) {
        return chatService.getListChat(usernameReceiver);
    }

    @PostMapping
    public ResponseEntity<CommonResponse> sendChat(@RequestParam("usernameReceiver") String usernameReceiver,
                                                   @RequestParam("message") String message,
                                                   @RequestParam(value = "file", required = false) MultipartFile file) {

        ChatRequest chatRequest = ChatRequest.builder()
                .usernameReceiver(usernameReceiver)
                .message(message)
                .file(file)
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
