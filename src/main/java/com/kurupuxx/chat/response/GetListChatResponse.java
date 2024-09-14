package com.kurupuxx.chat.response;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GetListChatResponse {
    private String message;
    private Date timestamp;
    private List<ChatResponse> chats;
}
