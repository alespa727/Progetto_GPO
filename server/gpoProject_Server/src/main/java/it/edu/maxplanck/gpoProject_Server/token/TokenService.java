package it.edu.maxplanck.gpoProject_Server.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import it.edu.maxplanck.gpoProject_Server.util.UtilToken;

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
	
	
	// Errore input -> Throws
	private String checkDatagetToken(String subject, Map<String, Object> claims, Key key, long expiration) throws NullPointerException {
		
		if(subject == null) throw new NullPointerException("Errore valore null");
		if(claims == null) throw new NullPointerException("Errore valore null");
		
		String token = this.generateToken(subject, claims, key, expiration, UtilToken.algorithm);
		return token;
	}
	
	public String getTokenAccess(String subject, Map<String, Object> claims) {
		
		String token = null;
		try {
			token = this.checkDatagetToken(subject, claims, keyAccess, UtilToken.timeExpirationDateAccessToken);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}
	
	public String getTokenRefresh(String subject, Map<String, Object> claims) {
		
		String token = null;
		try {
			token = this.checkDatagetToken(subject, claims, keyRefresh, UtilToken.timeExpirationDateRefreshToken);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}
	
	
	private Claims checkDataobtainClaims(String token, Key key) throws NullPointerException {
		
		if(token == null) throw new NullPointerException("Errore valore null");
		
		Claims claims = this.obtainTokenClaims(token, key);
		
		return claims;
	}
	
	public Claims getClaimsAccess(String token) {
		
		Claims claims = null;
		try {
			claims = this.checkDataobtainClaims(token, keyAccess);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return claims;
	}
	
	public Claims getClaimsRefresh(String token) {
		
		Claims claims = null;
		try {
			claims = this.checkDataobtainClaims(token, keyRefresh);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return claims;
	}

	
	public boolean isTokenAccessValid(String token) {
		return this.isTokenValid(token, keyAccess);
	}
	
	public boolean isTokenRefreshValid(String token) {
		return this.isTokenValid(token, keyRefresh);
	}
}
