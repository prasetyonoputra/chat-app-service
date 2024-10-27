package com.kurupuxx.chat.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
	private int statusCode;
	private String message;
	private T data;
	private boolean isError;
}
