package com.kurupuxx.chat.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
	private int statusCode;
	private String message;
	private Object data;
	private boolean isError;
}
