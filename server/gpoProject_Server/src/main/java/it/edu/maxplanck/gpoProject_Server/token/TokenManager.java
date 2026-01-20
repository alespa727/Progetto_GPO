package it.edu.maxplanck.gpoProject_Server.token;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

public class TokenManager implements TokenFunc {

	// Genera un token
	@Override
	public String generateToken(String subject, Map<String, Object> claims, Key key,  long expirationDate, SignatureAlgorithm algorithm) throws InvalidKeyException {
		// TODO Auto-generated method stub
		return Jwts.builder()
				.setSubject(subject)
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationDate))
				.signWith(key, algorithm)
				.compact();
	}

	// Ottiene il subject di un token se valido, se no null
	@Override
	public String obtainTokenSubject(String token, Key key) throws UnsupportedJwtException {
		// TODO Auto-generated method stub

		if(!isTokenValid(token, key)) return null;
		
		return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
	}

	// Ottieni i campi di un token se valido, se no null
	@Override
	public Claims obtainTokenClaims(String token, Key key) throws UnsupportedJwtException, MalformedJwtException, SignatureException, ExpiredJwtException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
		if(!isTokenValid(token, key)) return null;
		
		return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
	}

	// Controlla se il token e' valido o no
	@Override
	public boolean isTokenValid(String token, Key key) {
		// TODO Auto-generated method stub
		
		try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);    
        } catch (JwtException e) {
            return false;
        }
		
		return true;
	}
}
