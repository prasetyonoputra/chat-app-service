package com.kurupuxx.cakap.service;

import com.kurupuxx.cakap.request.ChatRequest;
import com.kurupuxx.cakap.response.CommonResponse;
import com.kurupuxx.cakap.response.GetListChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    ResponseEntity<CommonResponse> sendMessage(ChatRequest chatRequest);
    ResponseEntity<GetListChatResponse> getListChat(String usernameReceiver);
}
