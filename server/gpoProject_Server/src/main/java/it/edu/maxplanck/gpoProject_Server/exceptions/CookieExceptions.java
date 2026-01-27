package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum CookieExceptions {

	COOKIES_COOKIE_NOT_FOUND("Cookie non trovato", HttpStatus.NOT_FOUND),
	COOKIES_COOKIES_NOT_FOUND("Cookies non trovati", HttpStatus.NOT_FOUND),
	COOKIES_COOKIE_NOT_VALID("Cookie non valido", HttpStatus.NOT_ACCEPTABLE);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private CookieExceptions(String message, HttpStatus httpStatus) {
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
