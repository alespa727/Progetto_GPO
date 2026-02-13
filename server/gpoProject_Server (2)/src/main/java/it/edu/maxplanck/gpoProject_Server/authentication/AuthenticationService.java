package it.edu.maxplanck.gpoProject_Server.authentication;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.exceptions.*;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Classe che offre i servizi di autentificazione
 */
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
    
    public int authenticate(HttpServletRequest request, HttpServletResponse response) throws CookieException, TokenException, AuthentificationException {

		/*
		 * Controllo se ha cookies/ cookies non validi:
		 * 		- No -> Errore
		 */
		ArrayList<String> cookiesNames = new ArrayList<String>();
		cookiesNames.add(UtilServer.accessCookieName);
		cookiesNames.add(UtilServer.refreshCookieName);
		HashMap<String, Cookie> cookies;
		
		/*
		 * Ottengo i cookies e controllo se esistono quelli necessari
		 */
		cookies = this.getCookieService().findCookies(request, cookiesNames);
		if(cookies == null) throw new CookieException(CookieExceptions.COOKIES_COOKIES_NOT_FOUND);
		
		Cookie access = cookies.get(UtilServer.accessCookieName);
		Cookie refresh = cookies.get(UtilServer.refreshCookieName);
		
		/*
		 * Controllo se il cookie di accesso deve essere rigenerato
		 */
		if(access == null || !this.getCookieService().isCookieValid(access) || !this.getTokenService().isTokenAccessValid(access.getValue())) {
			
			if (refresh == null || !this.getCookieService().isCookieValid(refresh) || !this.getTokenService().isTokenRefreshValid(refresh.getValue())) {
	            throw new CookieException(CookieExceptions.COOKIES_COOKIE_NOT_FOUND);
	        }
			
			access =  this.refreshCookieAccess(cookies.get(UtilServer.refreshCookieName));
		}
		
		/*
		 * Ottengo l'id dello user
		 */
		Integer id = this.getTokenService().getClaimsAccess(access.getValue()).get("id", Integer.class);
		
		if(id == null) throw new AuthentificationException(AuthentificationExceptions.AUTH_DATA_IS_NOT_VALID);
		
		// Refresh access se non valido
		if (cookies.get(UtilServer.accessCookieName) == null) response.addCookie(access);
		
		return id;
	}
}

