package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.exceptions;

import org.springframework.http.HttpStatus;

public enum TokenExceptionTypes {
	
	TOKEN_IS_NOT_VALID("token is not valid", HttpStatus.NOT_ACCEPTABLE),
	UNABLE_TO_GENERATE_TOKEN("unable to generate the token", HttpStatus.INTERNAL_SERVER_ERROR)
	;
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private TokenExceptionTypes(String message, HttpStatus httpStatus) {
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
