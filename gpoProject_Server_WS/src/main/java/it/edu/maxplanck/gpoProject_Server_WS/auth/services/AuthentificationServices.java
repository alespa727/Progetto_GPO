package it.edu.maxplanck.gpoProject_Server_WS.auth.services;

import it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthentificationServices {

	protected TokenService tokenService;
	protected CookieService cookieService;
	
	public AuthentificationServices(TokenService tokenService, CookieService cookieService) {
		super();
		this.tokenService = tokenService;
		this.cookieService = cookieService;
	}
	
	public Cookie refreshCookieAccess(Cookie refresh) {
		return null;
    }
	
	public void authentication(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
