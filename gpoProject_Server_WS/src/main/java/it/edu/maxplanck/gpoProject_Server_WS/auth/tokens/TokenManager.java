package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class TokenManager implements TokenMethods {

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
    public Claims retriveTokenClaims(String token, Key key) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        
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
