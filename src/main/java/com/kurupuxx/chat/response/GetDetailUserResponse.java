package com.kurupuxx.chat.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDetailUserResponse {
    private String message;
    private Date timestamp;
    private UserResponse user;
}
