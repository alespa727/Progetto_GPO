package it.edu.maxplanck.gpoProject_Server.token.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestUserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class ServiceToken {

    private final SecretKey key;
    private final long expirationMs = 1000 * 60 * 60; // 1 ora

    public ServiceToken(@Value("${jwt.secret}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    // Genera token JWT da un username + isAdmin
    public String getTokenFromUser(RequestUserDTO user) {
        return Jwts.builder()
                .setSubject(user.username())
                .claim("isAdmin", user.isAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    // Estrae username dal token
    public String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Estrae boolean isAdmin
    public boolean extractIsAdmin(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("isAdmin", Boolean.class);
    }

    // Controlla validità del token
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
