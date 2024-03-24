package com.kurupuxx.cakap.response;

import com.kurupuxx.cakap.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

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
