package com.kurupuxx.chat.utils;

import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseAppController {
	protected ResponseEntity<BaseResponse> toResponse(Object data) {
		return ResponseEntity.ok(BaseResponse.builder().data(data).statusCode(200).isError(false).build());
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, int statusCode) {
		return ResponseEntity.ok(BaseResponse.builder().data(data).statusCode(statusCode).isError(false).build());
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, String message) {
		return ResponseEntity
				.ok(BaseResponse.builder().data(data).message(message).statusCode(200).isError(false).build());
	}

	protected ResponseEntity<BaseResponse> toResponse(Object data, int statusCode, String message) {
		return ResponseEntity
				.ok(BaseResponse.builder().data(data).message(message).statusCode(statusCode).isError(false).build());
	}

	protected ResponseEntity<BaseResponse> toResponse(Exception exception) {
		logError(exception.getLocalizedMessage());

		return ResponseEntity.internalServerError().body(
				BaseResponse.builder().message(exception.getLocalizedMessage()).statusCode(500).isError(true).build());
	}

	protected ResponseEntity<BaseResponse> toResponse(Exception exception, int statusCode) {
		logError(exception.getLocalizedMessage());

		return ResponseEntity.internalServerError().body(BaseResponse.builder().message(exception.getLocalizedMessage())
				.statusCode(statusCode).isError(true).build());
	}


	private void logError(String message) {
        log.error("ERROR :: {}", message);
	}
}