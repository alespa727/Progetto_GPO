package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("api")
public abstract class BasicApiRestController {

	protected final TokenService tokenService;
	protected final CookieService cookieService;
	protected final DatabaseService databaseService;
	
	protected final String accessTokenSubject;
	protected final String accessCookieName;
	
	protected final String refreshTokenSubject;
	protected final String refreshCookieName;
	
	public BasicApiRestController(
			TokenService tokenService, 
			CookieService cookieService,
			DatabaseService databaseService, 
			String accessTokenSubject, 
			String accessCookieName,
			String refreshTokenSubject, 
			String refreshCookieName
		) {
		super();
		this.tokenService = tokenService;
		this.cookieService = cookieService;
		this.databaseService = databaseService;
		this.accessTokenSubject = accessTokenSubject;
		this.accessCookieName = accessCookieName;
		this.refreshTokenSubject = refreshTokenSubject;
		this.refreshCookieName = refreshCookieName;
	}

	@GetMapping("")
	public String HelloWorld() {
		return "Hello world form api!";
	}
	
	/**
	 * Eliminazione cookies
	 * @param request
	 * @param response
	 * @param cookiesName
	 */
	protected void clearCookies(HttpServletRequest request, HttpServletResponse response, String[] cookiesName) {
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) return;
		
		// Eliminazione cookies
		for (String cookieName : cookiesName) {
			Cookie cookie = this.cookieService.getCookie(cookies, cookieName);
			if (cookie != null) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0); // elimina il cookie
				response.addCookie(cookie);
			}
		}
	}
}
