package it.edu.maxplanck.gpoProject_Server_WS.auth.tokens;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import it.edu.maxplanck.gpoProject_Server_WS.auth.keys.KeyClass;
import it.edu.maxplanck.gpoProject_Server_WS.model.ModelTokens;

@Service
public class TokenService {
	
	private final KeyClass accessKey;
	private final KeyClass refreshKey;
	private final TokenManager tokenManager;
	
	private final String subjectAccessToken;
	private final String subjectRefreshToken;
    
    public TokenService(
    		@Value(ModelTokens.accessTokenPath) String secretAccess,
    		@Value(ModelTokens.refreshTokenPath) String secretRefresh,
    		@Value(ModelTokens.accessTokenSubjectPath) String subjectAccessToken,
    		@Value(ModelTokens.refreshTokenSubjectPath) String subjectRefreshToken,
    		TokenManager tokenManager
    ) {
    	this.accessKey = new KeyClass(secretAccess, ModelTokens.accessToken_TimeToLive);
    	this.refreshKey = new KeyClass(secretRefresh, ModelTokens.refreshToken_TimeToLive);
    	this.subjectAccessToken = subjectAccessToken;
    	this.subjectRefreshToken = subjectRefreshToken;
        this.tokenManager = tokenManager;
    }
    
    // Access Token
    public String generate_AccessToken(Map<String, Object> claims) throws InvalidKeyException {
    	return this.tokenManager.generateToken(claims, subjectAccessToken, accessKey.getKey(), accessKey.getExpiration(), accessKey.getAlgorithm());
    }
    
    public boolean isValid_AccessToken(String token) {
    	return this.tokenManager.isTokenValid(token, accessKey.getKey());
    }
    
    public Claims retriveClaims_AccessToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    	return this.tokenManager.retriveTokenClaims(token, accessKey.getKey());
    }
    
    // Refresh token
    public String generate_RefreshToken(Map<String, Object> claims) throws InvalidKeyException {
    	return this.tokenManager.generateToken(claims, this.subjectRefreshToken, refreshKey.getKey(), refreshKey.getExpiration(), refreshKey.getAlgorithm());
    }
    
    public boolean isValid_RefreshToken(String token) {
    	return this.tokenManager.isTokenValid(token, refreshKey.getKey());
    }
    
    public Claims retriveClaims_RefreshToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    	return this.tokenManager.retriveTokenClaims(token, refreshKey.getKey());
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

	public String getSubjectAccessToken() {
		return subjectAccessToken;
	}

	public String getSubjectRefreshToken() {
		return subjectRefreshToken;
	}
}
