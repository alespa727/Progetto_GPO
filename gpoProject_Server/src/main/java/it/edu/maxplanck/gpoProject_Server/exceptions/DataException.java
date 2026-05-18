package it.edu.maxplanck.gpoProject_Server.exceptions;

/**
 * Classe che permette di far scattare una eccezione runtime di tipo Data
 */
public class DataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final DataExceptions exception;
	
	public DataException(DataExceptions exception) {
		this.exception = exception;
	}
	
	public DataExceptions getExceptions() {
		return this.exception;
	}
}
