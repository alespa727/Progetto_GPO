package it.edu.maxplanck.gpoProject_Server.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenException;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenExceptions;
import it.edu.maxplanck.gpoProject_Server.util.UtilToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
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
        byte[] keyBytes = keyAccess.getEncoded();
        String keyBase64 = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Key in Base64: " + keyBase64);
        String keyUtf8 = new String(keyBytes, UtilToken.charset);
        System.out.println("Key as UTF-8: " + keyUtf8);

    }

    // Genera token di accesso
    public String generateTokenAccess(Map<String, Object> claims, String subject) throws TokenException {
        return generateTokenSafe(claims, subject, keyAccess, UtilToken.timeExpirationDateAccessToken);
    }

    // Genera token di refresh
    public String generateTokenRefresh(Map<String, Object> claims, String subject) throws TokenException {
        return generateTokenSafe(claims, subject, keyRefresh, UtilToken.timeExpirationDateRefreshToken);
    }

    // Recupera claims access
    public Claims getClaimsAccess(String token) throws TokenException {
        try {
            return obtainTokenClaims(token, keyAccess);
        } catch (JwtException e) {
            throw new TokenException(TokenExceptions.TOKENS_TOKEN_NOT_VALID);
        }
    }

    // Recupera claims refresh
    public Claims getClaimsRefresh(String token) throws TokenException {
        try {
            return obtainTokenClaims(token, keyRefresh);
        } catch (JwtException e) {
            throw new TokenException(TokenExceptions.TOKENS_TOKEN_NOT_VALID);
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
    private String generateTokenSafe(Map<String, Object> claims, String subject, Key key, long expiration) throws TokenException {
        try {
            return generateToken(claims, subject, key, expiration, UtilToken.algorithm);
        } catch (Exception e) {
            throw new TokenException(TokenExceptions.TOKENS_GENERATION_TOKEN_FAILED);
        }
    }
}
