package it.edu.maxplanck.gpoProject_Server.exceptions;

public class DatabaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final DatabaseExceptions exception;
	
	public DatabaseException(DatabaseExceptions exception) {
		this.exception = exception;
	}
	
	public DatabaseExceptions getExceptions() {
		return this.exception;
	}
}
