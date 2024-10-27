package com.kurupuxx.chat.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ChatRequest {
    private String usernameReceiver;
    private String message;
    private MultipartFile file;
}
