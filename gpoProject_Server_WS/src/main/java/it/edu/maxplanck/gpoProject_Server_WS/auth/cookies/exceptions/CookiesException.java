package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.exceptions;

public class CookiesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final CookieExceptionTypes cookieException;

	public CookiesException(CookieExceptionTypes cookieException) {
		super();
		this.cookieException = cookieException;
	}

	public CookieExceptionTypes getCookieException() {
		return cookieException;
	}
}
