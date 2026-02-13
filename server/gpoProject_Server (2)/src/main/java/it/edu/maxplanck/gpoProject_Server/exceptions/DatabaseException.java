package it.edu.maxplanck.gpoProject_Server.exceptions;

/**
 * Classe che permette di far scattare una eccezione runtime di tipo Database
 */
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
