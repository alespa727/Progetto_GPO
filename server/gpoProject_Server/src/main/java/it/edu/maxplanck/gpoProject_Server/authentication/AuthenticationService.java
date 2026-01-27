package it.edu.maxplanck.gpoProject_Server.authentication;


import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.exceptions.*;
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
    
    public Cookie refreshCookieAccess(Cookie refresh) throws CookieException, TokenException {
    	
    	if(refresh == null) throw new CookieException(CookieExceptions.COOKIES_COOKIE_NOT_FOUND);
    	if(!this.cookieService.isCookieValid(refresh)) throw new CookieException(CookieExceptions.COOKIES_COOKIE_NOT_FOUND);
    	if(!this.tokenService.isTokenRefreshValid(refresh.getValue())) throw new TokenException(TokenExceptions.TOKENS_TOKEN_NOT_VALID);
    	
    	Claims claims = this.tokenService.getClaimsRefresh(refresh.getValue());
    	String token = this.tokenService.generateTokenAccess(claims, UtilServer.accessTokenSubject);
    	
    	return this.cookieService.generateCookie(UtilServer.accessCookieName, token, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);
    }
}

