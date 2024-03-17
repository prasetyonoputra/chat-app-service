package com.kurupuxx.cakap.response;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GetListContactResponse {
    private String message;
    private Date timestamp;
    private List<UserResponse> contacts;
}
