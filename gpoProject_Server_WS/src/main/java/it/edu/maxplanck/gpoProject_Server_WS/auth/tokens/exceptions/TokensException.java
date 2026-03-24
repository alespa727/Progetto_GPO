package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.exceptions;

public class TokensException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final TokenExceptionTypes tokenException;

	public TokensException(TokenExceptionTypes tokenException) {
		super();
		this.tokenException = tokenException;
	}

	public TokenExceptionTypes getTokenException() {
		return tokenException;
	}
}
