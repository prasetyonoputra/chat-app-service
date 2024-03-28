package com.kurupuxx.cakap.response;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private String message;
    private String pathFile;
    private UserResponse userSender;
    private UserResponse userReceiver;

    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;
}
