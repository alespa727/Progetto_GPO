package it.edu.maxplanck.gpoProject_Server.authentication;


import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;

@Service
public class AuthenticationService {

    protected final TokenService tokenService;
    protected final CookieService cookieService;
    protected final AuthenticationRequestDTOService authenticationRequestDTOService;

    public AuthenticationService(
            TokenService tokenService,
            CookieService cookieService,
            AuthenticationRequestDTOService authenticationRequestDTOService
    ) {
        this.tokenService = tokenService;
        this.cookieService = cookieService;
        this.authenticationRequestDTOService = authenticationRequestDTOService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public CookieService getCookieService() {
        return cookieService;
    }

    public AuthenticationRequestDTOService getAuthenticationRequestDTOService() {
        return authenticationRequestDTOService;
    }
    
    public boolean shouldRefreshCookie(Cookie access, Cookie refresh) throws IllegalArgumentException {
    	
    	if(access == null || refresh == null) throw new IllegalArgumentException("Cookie non esistenti");
    	
    	if(access != null && this.cookieService.isCookieValid(access)) {
    		return false;
    	}
    	
    	if(refresh != null && this.cookieService.isCookieValid(refresh) && this.tokenService.isTokenRefreshValid(refresh.getValue())) {
    		return true;
    	}
    	
    	throw new IllegalArgumentException("Errore cookies");
    }
    
    public Cookie refreshCookieAccess(Cookie refresh) throws IllegalArgumentException {
    	
    	if(refresh == null) throw new IllegalArgumentException("Cookie refresh non esistente");
    	if(!this.cookieService.isCookieValid(refresh)) throw new IllegalArgumentException("Cookie refresh non valido");
    	if(!this.tokenService.isTokenRefreshValid(refresh.getValue())) throw new IllegalArgumentException("Token cookie refresh non valido");
    	
    	Claims claims = this.tokenService.getClaimsRefresh(refresh.getValue());
    	String token = this.tokenService.generateTokenAccess(claims, UtilServer.accessTokenSubject);
    	
    	return this.cookieService.generateCookie(UtilServer.accessCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);
    }
}

