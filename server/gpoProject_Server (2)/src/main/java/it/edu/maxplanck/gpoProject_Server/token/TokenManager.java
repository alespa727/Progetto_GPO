package it.edu.maxplanck.gpoProject_Server.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class TokenManager implements TokenFunc {

    @Override
    public String generateToken(Map<String, Object> claims, String subject, Key key, long expirationDate, SignatureAlgorithm algorithm) throws InvalidKeyException {
        
    	if(key == null || algorithm == null) throw new IllegalArgumentException("Dati invalidi");
    	
    	return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(key, algorithm)
                .compact();
    }

    @Override
    public Claims obtainTokenClaims(String token, Key key) throws JwtException {
        
    	if(token == null || key == null) return null;
    	
    	return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isTokenValid(String token, Key key) {
    	
    	if(token == null || key == null) return false;
    	
    	try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
