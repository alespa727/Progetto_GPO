package it.edu.maxplanck.gpoProject_Server.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import it.edu.maxplanck.gpoProject_Server.util.UtilToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Map;

@Service
public class TokenService extends TokenManager {

    private final Key keyAccess;
    private final Key keyRefresh;

    public TokenService(
            @Value(UtilToken.accessTokenPath) String secretAccess,
            @Value(UtilToken.refreshTokenPath) String secretRefresh
    ) {
        this.keyAccess = Keys.hmacShaKeyFor(secretAccess.getBytes(UtilToken.charset));
        this.keyRefresh = Keys.hmacShaKeyFor(secretRefresh.getBytes(UtilToken.charset));
    }

    // Genera token di accesso
    public String generateTokenAccess(Map<String, Object> claims, String subject) throws IllegalArgumentException {
        return generateTokenSafe(claims, subject, keyAccess, UtilToken.timeExpirationDateAccessToken);
    }

    // Genera token di refresh
    public String generateTokenRefresh(Map<String, Object> claims, String subject) throws IllegalArgumentException {
        return generateTokenSafe(claims, subject, keyRefresh, UtilToken.timeExpirationDateRefreshToken);
    }

    // Recupera claims access
    public Claims getClaimsAccess(String token) throws IllegalArgumentException {
        try {
            return obtainTokenClaims(token, keyAccess);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Access token non valido", e);
        }
    }

    // Recupera claims refresh
    public Claims getClaimsRefresh(String token) throws IllegalArgumentException {
        try {
            return obtainTokenClaims(token, keyRefresh);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Refresh token non valido", e);
        }
    }

    // Controlla se token access è valido
    public boolean isTokenAccessValid(String token) {
        return isTokenValid(token, keyAccess);
    }

    // Controlla se token refresh è valido
    public boolean isTokenRefreshValid(String token) {
        return isTokenValid(token, keyRefresh);
    }

    // Generatore token
    private String generateTokenSafe(Map<String, Object> claims, String subject, Key key, long expiration) throws IllegalArgumentException {
        try {
            return generateToken(claims, subject, key, expiration, UtilToken.algorithm);
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nella generazione del token", e);
        }
    }
}
