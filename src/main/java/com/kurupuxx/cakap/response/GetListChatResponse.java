package com.kurupuxx.cakap.response;

import com.kurupuxx.cakap.model.Chat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetListChatResponse {
    private String message;
    private Date timestamp;
    private List<ChatResponse> chats;
}
