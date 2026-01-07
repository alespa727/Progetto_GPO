package it.edu.maxplanck.gpoProject_Server.handler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.edu.maxplanck.gpoProject_Server.dto.response.ApiErrorResponse;
import it.edu.maxplanck.gpoProject_Server.exceptions.CustomException;

@RestControllerAdvice
public class Handler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException e) {

	    Map<String, String> errors = new HashMap<>();

	    e.getBindingResult().getFieldErrors()
	      .forEach(err -> errors.put(
	              err.getField(),
	              err.getDefaultMessage()
	      ));

	    ApiErrorResponse response = new ApiErrorResponse(
	            "VALIDATION_ERROR",
	            "Dati non validi",
	            400,
	            Instant.now(),
	            errors
	    );

	    return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(CustomException e) {

	    ApiErrorResponse response = new ApiErrorResponse(
	            e.getError(),
	            e.getMessage(),
	            e.getStatus().value(),
	            Instant.now(),
	            e.getDetails()
	    );

	    return ResponseEntity.status(e.getStatus())
	                         .body(response);
	}
	
	// Errore generico non previsto
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGeneric(Exception e) {

	    ApiErrorResponse response = new ApiErrorResponse(
	            "INTERNAL_ERROR",
	            "Errore interno del server",
	            500,
	            Instant.now(),
	            null
	    );

	    return ResponseEntity.internalServerError()
	                         .body(response);
	}
}
