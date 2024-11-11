package com.erp.exception.handler;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorResponse {

	private String message;
	private Integer httpStatus;
	private LocalDateTime dateTime;
}
