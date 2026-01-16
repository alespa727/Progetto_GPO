package it.edu.maxplanck.gpoProject_Server.exceptions;

public abstract class ClientErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message;
	protected int code;
	
	public ClientErrorException(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}
}
