package com.kurupuxx.chat.utils;

import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseAppController {
	protected <T> ResponseEntity<BaseResponse<T>> toResponse(T data) {
		return ResponseEntity.ok(BaseResponse.<T>builder().data(data).statusCode(200).isError(false).build());
	}

	protected <T> ResponseEntity<BaseResponse<T>> toResponse(T data, int statusCode) {
		return ResponseEntity.ok(BaseResponse.<T>builder().data(data).statusCode(statusCode).isError(false).build());
	}

	protected <T> ResponseEntity<BaseResponse<T>> toResponse(T data, String message) {
		return ResponseEntity
				.ok(BaseResponse.<T>builder().data(data).message(message).statusCode(200).isError(false).build());
	}

	protected <T> ResponseEntity<BaseResponse<T>> toResponse(T data, int statusCode, String message) {
		return ResponseEntity.ok(
				BaseResponse.<T>builder().data(data).message(message).statusCode(statusCode).isError(false).build());
	}

	protected <T> ResponseEntity<BaseResponse<T>> toResponse(Exception exception) {
		log.error("ERROR :: " + exception.getLocalizedMessage());

		return ResponseEntity.internalServerError().body(BaseResponse.<T>builder()
				.message(exception.getLocalizedMessage()).statusCode(500).isError(true).build());
	}

	protected <T> ResponseEntity<BaseResponse<T>> toResponse(Exception exception, int statusCode) {
		log.error("ERROR :: " + exception.getLocalizedMessage());

		return ResponseEntity.internalServerError().body(BaseResponse.<T>builder()
				.message(exception.getLocalizedMessage()).statusCode(statusCode).isError(true).build());
	}
}