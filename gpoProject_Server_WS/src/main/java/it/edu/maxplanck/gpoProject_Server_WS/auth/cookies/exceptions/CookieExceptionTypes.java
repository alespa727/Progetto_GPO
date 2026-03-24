package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.exceptions;

import org.springframework.http.HttpStatus;

public enum CookieExceptionTypes {
	
	COOKIE_NOT_FOUND("cookie not found", HttpStatus.NOT_FOUND),
	COOKIES_NOT_FOUND("cookies not found", HttpStatus.NOT_FOUND),
	COOKIE_IS_NOT_VALID("cookie is not valid", HttpStatus.NOT_ACCEPTABLE)
	;
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private CookieExceptionTypes(String message, HttpStatus httpStatus) {
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
