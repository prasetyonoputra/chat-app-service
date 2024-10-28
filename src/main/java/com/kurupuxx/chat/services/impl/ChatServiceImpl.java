package com.kurupuxx.chat.services.impl;

import com.kurupuxx.chat.entities.Chat;
import com.kurupuxx.chat.entities.Contact;
import com.kurupuxx.chat.entities.User;
import com.kurupuxx.chat.repositories.ChatRepository;
import com.kurupuxx.chat.repositories.ContactRepository;
import com.kurupuxx.chat.repositories.UserRepository;
import com.kurupuxx.chat.requests.ChatRequest;
import com.kurupuxx.chat.response.ChatResponse;
import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.GetListChatResponse;
import com.kurupuxx.chat.response.UserResponse;
import com.kurupuxx.chat.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Override
    public ResponseEntity<CommonResponse> sendMessage(ChatRequest chatRequest) {
        CommonResponse response = new CommonResponse();
        response.setTimestamp(new Date());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            response.setMessage("User login not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User user = userOptional.get();

        Optional<User> userReceiverOptional = userRepository.findByUsername(chatRequest.getUsernameReceiver());
        if (userReceiverOptional.isEmpty()) {
            response.setMessage("User receiver not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User userReceiver = userReceiverOptional.get();

        Optional<Contact> verifyContactOptional = contactRepository.findContactByAcceptorAndReceptor(user, userReceiver);
        if (verifyContactOptional.isEmpty()) {
            response.setMessage("User receiver not found in contact!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Chat chat = Chat.builder()
                .message(chatRequest.getMessage())
                .userReceiver(userReceiver)
                .userSender(user)
                .createdAt(new Date())
                .updatedAt(new Date())
                .updatedBy(user.getUsername())
                .build();

        chatRepository.save(chat);
        response.setMessage("Success send message!");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetListChatResponse> getListChat(String usernameReceiver) {
        GetListChatResponse response = new GetListChatResponse();
        response.setTimestamp(new Date());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            response.setMessage("User login not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User user = userOptional.get();

        Optional<User> userReceiverOptional = userRepository.findByUsername(usernameReceiver);
        if (userReceiverOptional.isEmpty()) {
            response.setMessage("User receiver not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User userReceiver = userReceiverOptional.get();

        List<Chat> rawChats = chatRepository.findByUserSenderAndUserReceiver(user, userReceiver);
        List<ChatResponse> chats = new ArrayList<>();

        for (Chat chat : rawChats) {
            chats.add(ChatResponse.builder()
                            .message(chat.getMessage())
                            .pathFile(chat.getPathFile())
                            .userSender(convertToUserResponse(chat.getUserSender()))
                            .userReceiver(convertToUserResponse(chat.getUserReceiver()))
                            .createdAt(chat.getCreatedAt())
                            .updatedAt(chat.getUpdatedAt())
                            .updatedBy(chat.getUpdatedBy())
                    .build());
        }

        response.setChats(chats);
        response.setMessage("Success get list chat!");

        return ResponseEntity.ok(response);
    }

    public UserResponse convertToUserResponse (User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .status(user.getStatus())
                .build();
    }
}
