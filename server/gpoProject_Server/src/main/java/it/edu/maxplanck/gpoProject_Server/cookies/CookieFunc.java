package it.edu.maxplanck.gpoProject_Server.cookies;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieFunc {

	// Genera un cookie
	public Cookie generateCookie(String name, String value, boolean httpOnly, boolean secure, String path, int maxAge);
	
	// Ottiene una mappa di cookies richiesti
	public HashMap<String, Cookie> findCookies(HttpServletRequest request, ArrayList<String> cookiesName);
	
	// Aggiorna un cookie
	public void updateCookie(Cookie cookie, String value, Boolean httpOnly, Boolean secure, String path, Integer maxAge);
	
	// Controlla se un cookie e' valido
	public boolean isCookieValid(Cookie cookie);
	
	// Elimina un cookie
	public void removeCookie(Cookie cookie, String path);
}
