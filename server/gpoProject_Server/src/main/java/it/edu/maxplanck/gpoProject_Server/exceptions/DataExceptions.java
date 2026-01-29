package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum DataExceptions {
	
	DATA_FILES_NOT_VALID("File non accettati poiche' pericolosi", HttpStatus.BAD_REQUEST),
	DATA_IMAGE_NOT_FOUND("Immagine non trovata, puo' essere stata persa", HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private DataExceptions(String message, HttpStatus httpStatus) {
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
