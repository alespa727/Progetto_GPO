package it.edu.maxplanck.gpoProject_Server.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Map;

public interface TokenFunc {

	// Genera un token
	public String generateToken(String subject, Map<String, Object> claims, Key key, long expirationDate, SignatureAlgorithm algorithm);
	
	// Ottiene il subject di un token
	public String obtainTokenSubject(String token, Key key);
	
	// Ottiene i claim di un token
	public Claims obtainTokenClaims(String token, Key key);
	
	// Controlla se il token e' valido o no
	public boolean isTokenValid(String token, Key key);
}
