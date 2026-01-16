package it.edu.maxplanck.gpoProject_Server.exceptions;

public class ServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message;
	protected int statusCode;
	
	public ServerException(String message, int statusCode) {
		super();
		this.message = message;
		this.statusCode = statusCode;
	}
}
