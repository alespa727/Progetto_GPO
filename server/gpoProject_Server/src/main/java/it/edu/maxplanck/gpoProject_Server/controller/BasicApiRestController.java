package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.exceptions.AuthentificationException;
import it.edu.maxplanck.gpoProject_Server.exceptions.AuthentificationExceptions;
import it.edu.maxplanck.gpoProject_Server.exceptions.CookieException;
import it.edu.maxplanck.gpoProject_Server.exceptions.CookieExceptions;
import it.edu.maxplanck.gpoProject_Server.exceptions.TokenException;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("api")
public abstract class BasicApiRestController {
	
	@Value("${app.upload.dir}")
	protected String uploadDir;
	
	protected final DatabaseService databaseService;
	protected final AuthenticationService authenticationService;
	
	public BasicApiRestController(DatabaseService databaseService, AuthenticationService authenticationService) {
		super();
		this.databaseService = databaseService;
		this.authenticationService = authenticationService;
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
		cookies = this.authenticationService.getCookieService().findCookies(request, cookiesNames);
		if(cookies == null) throw new CookieException(CookieExceptions.COOKIES_COOKIES_NOT_FOUND);
		
		Cookie access = cookies.get(UtilServer.accessCookieName);
		Cookie refresh = cookies.get(UtilServer.refreshCookieName);
		
		/*
		 * Controllo se il cookie di accesso deve essere rigenerato
		 */
		if(access == null || !this.authenticationService.getCookieService().isCookieValid(access) || !this.authenticationService.getTokenService().isTokenAccessValid(access.getValue())) {
			
			if (refresh == null || !this.authenticationService.getCookieService().isCookieValid(refresh) || !this.authenticationService.getTokenService().isTokenRefreshValid(refresh.getValue())) {
	            throw new CookieException(CookieExceptions.COOKIES_COOKIE_NOT_FOUND);
	        }
			
			access =  this.authenticationService.refreshCookieAccess(cookies.get(UtilServer.refreshCookieName));
		}
		
		/*
		 * Ottengo l'id dello user
		 */
		Integer id = this.authenticationService.getTokenService().getClaimsAccess(access.getValue()).get("id", Integer.class);
		
		if(id == null) throw new AuthentificationException(AuthentificationExceptions.AUTH_DATA_IS_NOT_VALID);
		
		// Refresh access se non valido
		if (cookies.get(UtilServer.accessCookieName) == null) response.addCookie(access);
		
		return id;
	}
}
