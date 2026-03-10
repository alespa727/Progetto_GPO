package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import it.edu.maxplanck.gpoProject_Server_WS.auth.keys.KeyClass;
import it.edu.maxplanck.gpoProject_Server_WS.auth.keys.ModelKeys;

@Service
public class TokenService {
	
	private final KeyClass accessKey;
	private final KeyClass refreshKey;
	private final TokenManager tokenManager;
    
    public TokenService(
    		@Value(ModelKeys.accessTokenPath) String secretAccess,
    		@Value(ModelKeys.refreshTokenPath) String secretRefresh,
    		TokenManager tokenManager
    ) {
    	this.accessKey = new KeyClass(secretAccess, ModelKeys.accessToken_TimeToLive);
    	this.refreshKey = new KeyClass(secretRefresh, ModelKeys.refreshToken_TimeToLive);
        this.tokenManager = tokenManager;
    }
    
    public String generate_AccessToken(Map<String, Object> claims, String subject) throws InvalidKeyException {
    	return this.tokenManager.generateToken(claims, subject, accessKey.getKey(), accessKey.getExpiration(), accessKey.getAlgorithm());
    }
    
    public boolean isValid_AccessToken(String token) {
    	return this.tokenManager.isTokenValid(token, accessKey.getKey());
    }
    
    public Claims retriveClaims_AccessToken(String token) throws JwtException {
    	return this.tokenManager.obtainTokenClaims(token, accessKey.getKey());
    }
    
    public String generate_RefreshToken(Map<String, Object> claims, String subject) throws InvalidKeyException {
    	return this.tokenManager.generateToken(claims, subject, refreshKey.getKey(), refreshKey.getExpiration(), refreshKey.getAlgorithm());
    }
    
    public boolean isValid_RefreshToken(String token) {
    	return this.tokenManager.isTokenValid(token, refreshKey.getKey());
    }
    
    public Claims retriveClaims_RefreshToken(String token) throws JwtException {
    	return this.tokenManager.obtainTokenClaims(token, refreshKey.getKey());
    }

	public KeyClass getAccessKey() {
		return accessKey;
	}

	public KeyClass getRefreshKey() {
		return refreshKey;
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}
}
