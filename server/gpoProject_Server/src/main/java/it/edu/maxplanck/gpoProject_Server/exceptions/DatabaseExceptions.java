package it.edu.maxplanck.gpoProject_Server.exceptions;

import org.springframework.http.HttpStatus;

public enum DatabaseExceptions {
	
	DB_DATA_INSERTED_IS_NOT_VALID("Dati inseriti non validi", HttpStatus.BAD_REQUEST),
	DB_USER_NOT_FOUND("Utente non trovato", HttpStatus.NOT_FOUND),
	DB_CHAT_NOT_FOUND("Chat non trovata", HttpStatus.NOT_FOUND),
	DB_USER_IS_NOT_PART_OF_CHAT("L'utente non fa parte della chat", HttpStatus.FORBIDDEN),
	DB_CREDENTIALS_ARE_INCORRECT("Credenziali errate", HttpStatus.BAD_REQUEST),
	DB_USERNAME_IS_ALREADY_IN_USE("Username gia' in uso", HttpStatus.CONFLICT),
	DB_FRIENDSHIP_ALREADY_CREATED("Amicizia gia' creata", HttpStatus.CONFLICT),
	DB_CHAT_ALREADY_CREATED("Chat gia' creata", HttpStatus.CONFLICT),
	DB_CALL_STILL_OPEN("Una altra chiamata ancora attiva", HttpStatus.CONFLICT), 
	DB_FRIENDSHIP_NOT_CREATED("Amicizia non trovata", HttpStatus.NOT_FOUND),
	DB_ATTACHED_NOT_FOUND("Allegato non trovato", HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus responseStatus;
	
	private DatabaseExceptions(String message, HttpStatus httpStatus) {
		this.message = message;
		this.responseStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getResponseStatus() {
		return responseStatus;
	}
}
