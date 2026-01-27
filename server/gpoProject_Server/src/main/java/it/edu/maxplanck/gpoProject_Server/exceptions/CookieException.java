package it.edu.maxplanck.gpoProject_Server.exceptions;

public class CookieException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final CookieExceptions exception;
	
	public CookieException(CookieExceptions exception) {
		this.exception = exception;
	}
	
	public CookieExceptions getExceptions() {
		return this.exception;
	}
}
