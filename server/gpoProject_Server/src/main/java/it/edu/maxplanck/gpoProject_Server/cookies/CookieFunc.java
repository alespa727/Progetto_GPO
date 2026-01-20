package it.edu.maxplanck.gpoProject_Server.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieFunc {

	// Genera un cookie
	public Cookie generateCookie(String name, String value, boolean httpOnly, boolean secure, String path, int maxAge);
	
	// Ottiene i cookies
	public Cookie[] getCookies(HttpServletRequest request);
	
	// Ricerca un determinato cookie
	public Cookie getCookie(Cookie[] cookies, String name);
}
