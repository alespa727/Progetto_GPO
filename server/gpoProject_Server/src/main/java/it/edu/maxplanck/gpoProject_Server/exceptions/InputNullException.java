package it.edu.maxplanck.gpoProject_Server.exceptions;

public class InputNullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message;
	protected int code;
	
	public InputNullException(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}
}
