package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens;

import java.security.Key;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

public interface TokenMethods {

    String generateToken(Map<String, Object> claims, String subject, Key key, long expirationDate, SignatureAlgorithm algorithm);

    Claims retriveTokenClaims(String token, Key key);

    boolean isTokenValid(String token, Key key);
}
