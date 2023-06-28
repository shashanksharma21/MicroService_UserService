package com.user.service.request.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonErrorResponse {

	private String message;
	private Boolean success;
	private HttpStatus status;
	private Integer statusCode;
	
}
