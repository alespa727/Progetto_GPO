package it.edu.maxplanck.gpoProject_Server_WS.auth.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.exceptions.CookieExceptionTypes;
import it.edu.maxplanck.gpoProject_Server_WS.auth.cookies.exceptions.CookiesException;
import it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.TokenService;
import it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.exceptions.TokenExceptionTypes;
import it.edu.maxplanck.gpoProject_Server_WS.auth.tokens.exceptions.TokensException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthentificationServices {

	protected TokenService tokenService;
	protected CookieService cookieService;
	
	public AuthentificationServices(TokenService tokenService, CookieService cookieService) {
		super();
		this.tokenService = tokenService;
		this.cookieService = cookieService;
	}
	
	public Cookie refreshCookieAccess(Cookie refresh) {
		
		if(refresh == null) throw new CookiesException(CookieExceptionTypes.COOKIE_NOT_FOUND);
		if(!this.cookieService.getCookieManager().isCookieValid(refresh)) throw new CookiesException(CookieExceptionTypes.COOKIE_IS_NOT_VALID);
		if(!this.tokenService.isValid_AccessToken(refresh.getValue())) throw new TokensException(TokenExceptionTypes.TOKEN_IS_NOT_VALID);
		
		Claims claims = this.tokenService.retriveClaims_RefreshToken(refresh.getValue());
    	String token = this.tokenService.generate_AccessToken(claims);
    	
		return this.cookieService.generate_AccessCookie(token);
    }
	
	public Integer authentication(HttpServletRequest request, HttpServletResponse response) {
		
		ArrayList<String> cookiesNames = new ArrayList<String>();
		cookiesNames.add(this.cookieService.getAccessCookie().getCookieName());
		cookiesNames.add(this.cookieService.getRefreshCookie().getCookieName());
		ArrayList<Cookie> cookies;
		
		/*
		 * Ottengo i cookies e controllo se esistono quelli necessari
		 */
		cookies = this.cookieService.getCookieManager().findCookies(request, cookiesNames);
		if(cookies == null) throw new CookiesException(CookieExceptionTypes.COOKIES_NOT_FOUND);
		
		Cookie access;
		Cookie refresh;
		{
			int posAccess = this.cookieService.getCookieManager().retrivePositionCookie(cookiesNames, this.cookieService.getAccessCookie().getCookieName());
			int posRefresh = this.cookieService.getCookieManager().retrivePositionCookie(cookiesNames, this.cookieService.getRefreshCookie().getCookieName());
			
			if(posAccess == -1 || posRefresh == -1) throw new CookiesException(CookieExceptionTypes.COOKIE_NOT_FOUND);
			
			access = cookies.get(posAccess);
			refresh = cookies.get(posRefresh);
		}
		
		
		/*
		 * Controllo se il cookie di accesso deve essere rigenerato
		 */
		if(access == null || !this.cookieService.getCookieManager().isCookieValid(access) || !this.tokenService.isValid_AccessToken(access.getValue())) {
			
			if (refresh == null || !this.cookieService.getCookieManager().isCookieValid(refresh) || !this.tokenService.isValid_RefreshToken(refresh.getValue())) {
	            return;
	        }
			
			access =  this.refreshCookieAccess(refresh);

			// Refresh access se non valido
			response.addCookie(access);
		}
		
		/*
		 * Ottengo l'id dello user
		 */
		Integer id;
		try{
			id = this.tokenService.retriveClaims_AccessToken(access.getValue()).get("id", Integer.class);
		}catch (JwtException e) {
			
		}
		
		return id; // return id;
	}
}
