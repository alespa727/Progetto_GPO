package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum ClientExceptions {

	REFRESH_COOKIE_NOT_FOUND("Cookie di refresh non trovato", HttpStatus.NOT_FOUND),
	COOKIES_NOT_FOUND("Cookies richiesti non trovati", HttpStatus.NOT_FOUND),
	USERNAME_ALREADY_EXISTS("Username already exists", HttpStatus.CONFLICT);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private ClientExceptions(String message, HttpStatus httpStatus) {
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
