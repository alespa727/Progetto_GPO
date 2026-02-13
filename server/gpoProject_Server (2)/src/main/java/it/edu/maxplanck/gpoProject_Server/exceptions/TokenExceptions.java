package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum TokenExceptions {

	TOKENS_TOKEN_NOT_VALID("Token di refresh non valido", HttpStatus.NOT_ACCEPTABLE),
	TOKENS_GENERATION_TOKEN_FAILED("Errore nella generazione del token", HttpStatus.INTERNAL_SERVER_ERROR);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private TokenExceptions(String message, HttpStatus httpStatus) {
		this.message = message;
		this.responseStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getResponseStatus() {
		return responseStatus;
	}
}

