package com.kurupuxx.chat.services;

import com.kurupuxx.chat.requests.ChatRequest;
import com.kurupuxx.chat.response.CommonResponse;
import com.kurupuxx.chat.response.GetListChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    ResponseEntity<CommonResponse> sendMessage(ChatRequest chatRequest);
    ResponseEntity<GetListChatResponse> getListChat(String usernameReceiver);
}
