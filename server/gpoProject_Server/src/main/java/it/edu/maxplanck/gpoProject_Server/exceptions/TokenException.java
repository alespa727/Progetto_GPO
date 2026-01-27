package it.edu.maxplanck.gpoProject_Server.exceptions;

public class TokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final TokenExceptions exception;
	
	public TokenException(TokenExceptions exception) {
		this.exception = exception;
	}
	
	public TokenExceptions getExceptions() {
		return this.exception;
	}
}
