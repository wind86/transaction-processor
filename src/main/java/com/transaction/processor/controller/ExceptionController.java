package com.transaction.processor.controller;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, ?>> handleValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		
		Map<String, String> fieldErrors = result.getFieldErrors().stream()
			.collect(toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
		
		return new ResponseEntity<>(singletonMap("errors", fieldErrors), BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,?>> handleException(Exception e) {
		return new ResponseEntity<>(singletonMap("error", getRootCauseMessage(e)), INTERNAL_SERVER_ERROR);
	}
}