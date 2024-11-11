package com.erp.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.erp.exception.HandleAuthException;
import com.erp.exception.HandleLockedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(HandleLockedException.class)
	public ResponseEntity<ErrorResponse> handleLockedException(HandleLockedException ex) {
		return ResponseEntity.status(HttpStatus.LOCKED).body(
				ErrorResponse.builder()
					.message(ex.getMessage())
					.dateTime(LocalDateTime.now())
					.httpStatus(HttpStatus.LOCKED.value())
					.build()
				);
	}

	@ExceptionHandler(HandleAuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(HandleAuthException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				ErrorResponse.builder()
					.message(ex.getMessage())
					.dateTime(LocalDateTime.now())
					.httpStatus(HttpStatus.UNAUTHORIZED.value())
					.build()
				);
	}
}
