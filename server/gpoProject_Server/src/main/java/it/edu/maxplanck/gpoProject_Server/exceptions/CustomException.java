package it.edu.maxplanck.gpoProject_Server.exceptions;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private final String error;
	private final String message;
	private final HttpStatus status;
	private final Map<String, String> details;

	public CustomException(String error, String message, HttpStatus status, Map<String, String> details) {
		super();
		this.error = error;
		this.message = message;
		this.status = status;
		this.details = details;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public Map<String, String> getDetails() {
		return details;
	}
}
