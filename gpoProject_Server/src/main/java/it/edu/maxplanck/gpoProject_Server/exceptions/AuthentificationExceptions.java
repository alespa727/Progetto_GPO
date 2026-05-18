package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum AuthentificationExceptions {
	
	AUTH_DATA_IS_NOT_VALID("Dati non validi", HttpStatus.NOT_ACCEPTABLE),
	AUTH_INSERTED_DTO_DATA_IS_NOT_VALID("I dati inseriti non sono validi", HttpStatus.BAD_REQUEST);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private AuthentificationExceptions(String message, HttpStatus httpStatus) {
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
