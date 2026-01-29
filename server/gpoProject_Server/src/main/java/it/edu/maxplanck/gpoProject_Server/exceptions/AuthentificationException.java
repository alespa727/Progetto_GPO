package it.edu.maxplanck.gpoProject_Server.exceptions;

/**
 * Classe che permette di far scattare una eccezione runtime di tipo Authentification
 */
public class AuthentificationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final AuthentificationExceptions exception;
	
	public AuthentificationException(AuthentificationExceptions exception) {
		this.exception = exception;
	}
	
	public AuthentificationExceptions getExceptions() {
		return this.exception;
	}
}
