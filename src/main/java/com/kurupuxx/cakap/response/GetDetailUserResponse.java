package com.kurupuxx.cakap.response;

import java.util.Date;

import com.kurupuxx.cakap.model.User;

import lombok.Data;

@Data
public class GetDetailUserResponse {
    private String message;
    private Date timestamp;
    private User user;
}
