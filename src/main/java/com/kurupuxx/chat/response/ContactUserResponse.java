package com.kurupuxx.chat.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactUserResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String status;
    private String confirmationStatus;
}
