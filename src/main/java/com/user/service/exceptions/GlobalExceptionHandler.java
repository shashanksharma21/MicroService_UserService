package com.user.service.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;

import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.service.response.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler<T> {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<T> handelResourceNotFoundException(ResourceNotFoundException exception) {
		return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(exception.getMessage(), false, HttpStatus.NOT_FOUND, 404));
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<T> handelSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
		return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(exception.getMessage(), false, HttpStatus.INTERNAL_SERVER_ERROR, 500));
	}
	
	@ExceptionHandler(PropertyValueException.class)
	public ResponseEntity<T> handelPropertyValueException(PropertyValueException exception) {
		return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("Please enter valid "+exception.getPropertyName(), false, HttpStatus.INTERNAL_SERVER_ERROR, 500));
	}
}
