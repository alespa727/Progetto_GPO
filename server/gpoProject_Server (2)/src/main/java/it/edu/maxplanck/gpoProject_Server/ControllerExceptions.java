package it.edu.maxplanck.gpoProject_Server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.edu.maxplanck.gpoProject_Server.exceptions.AuthentificationException;
import it.edu.maxplanck.gpoProject_Server.exceptions.CookieException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DatabaseException;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenException;

/**
 * Classe che gestisce le eccezioni indicate
 */
@RestControllerAdvice
public class ControllerExceptions {

    @ExceptionHandler(CookieException.class)
    public ResponseEntity<String> handleCookieException(CookieException e) {
        return ResponseEntity
                .status(e.getExceptions().getResponseStatus())
                .body(e.getExceptions().getMessage());
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<String> handleCookieException(DataException e) {
        return ResponseEntity
                .status(e.getExceptions().getResponseStatus())
                .body(e.getExceptions().getMessage());
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleCookieException(DatabaseException e) {
        return ResponseEntity
                .status(e.getExceptions().getResponseStatus())
                .body(e.getExceptions().getMessage());
    }
    
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenException e) {
        return ResponseEntity
                .status(e.getExceptions().getResponseStatus())
                .body(e.getExceptions().getMessage());
    }

    @ExceptionHandler(AuthentificationException.class)
    public ResponseEntity<String> handleAuthException(AuthentificationException e) {
        return ResponseEntity
                .status(e.getExceptions().getResponseStatus())
                .body(e.getExceptions().getMessage());
    }
}
