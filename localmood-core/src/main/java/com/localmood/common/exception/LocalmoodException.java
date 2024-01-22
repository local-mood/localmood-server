package com.localmood.common.exception;

import lombok.Getter;

@Getter
public class LocalmoodException extends RuntimeException {
	private int status;
	private String message;
	private String solution;

	public LocalmoodException(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getHttpStatus().value();
		this.solution = errorCode.getSolution();
	}

	public LocalmoodException(ErrorCode errorCode, String message) {
		this.message = message;
		this.status = errorCode.getHttpStatus().value();
		this.solution = errorCode.getSolution();
	}

	public LocalmoodException(ErrorCode errorCode, String message, String solution) {
		this.message = message;
		this.status = errorCode.getHttpStatus().value();
		this.solution = solution;
	}
}