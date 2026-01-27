package it.edu.maxplanck.gpoProject_Server.exceptions;

public class ClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ClientExceptions exception;
	
	public ClientException(ClientExceptions exception) {
		this.exception = exception;
	}
	
	public ClientExceptions getExceptions() {
		return this.exception;
	}
}

