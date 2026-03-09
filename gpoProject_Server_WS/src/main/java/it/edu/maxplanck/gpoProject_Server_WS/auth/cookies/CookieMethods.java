package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

import java.util.ArrayList;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieMethods {
	
	public Cookie generateCookie(String name, String value, boolean httpOnly, boolean secure, String path, int maxAge);
	
	public ArrayList<Cookie> findCookies(HttpServletRequest request, ArrayList<String> cookiesName);
	
	public void updateCookie(Cookie cookie, String value, Boolean httpOnly, Boolean secure, String path, Integer maxAge);
	
	public boolean isCookieValid(Cookie cookie);
	
	public void removeCookie(Cookie cookie, String path);
}
