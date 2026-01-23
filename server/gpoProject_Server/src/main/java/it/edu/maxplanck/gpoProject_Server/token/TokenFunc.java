package it.edu.maxplanck.gpoProject_Server.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Map;

public interface TokenFunc {

    // Genera un token
    String generateToken(Map<String, Object> claims, String subject, Key key, long expirationDate, SignatureAlgorithm algorithm);

    // Ottiene i claim di un token
    Claims obtainTokenClaims(String token, Key key);

    // Controlla se il token è valido
    boolean isTokenValid(String token, Key key);
}
