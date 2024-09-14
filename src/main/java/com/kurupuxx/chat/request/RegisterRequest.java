package com.kurupuxx.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private MultipartFile imageProfile;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
