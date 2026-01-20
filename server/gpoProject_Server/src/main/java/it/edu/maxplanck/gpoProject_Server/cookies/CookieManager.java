package it.edu.maxplanck.gpoProject_Server.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieManager implements CookieFunc {

	// Generatore di cookie
	@Override
	public Cookie generateCookie(String name, String value, boolean isHttpOnly, boolean isSecure, String path, int maxAge) {
		// TODO Auto-generated method stub
		
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(isHttpOnly);
		cookie.setSecure(isSecure); // true in produzione
		cookie.setPath(path);
		cookie.setMaxAge(maxAge); // in secondi
		
		return cookie;
	}

	// Ottieni tutti i cookie
	@Override
	public Cookie[] getCookies(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Cookie[] cookies = request.getCookies();
		return cookies;
	}

	// Se cookie valido e trovato allora ritorna il cookie, se no ritorna null
	@Override
	public Cookie getCookie(Cookie[] cookies, String name) {
		// TODO Auto-generated method stub
		
		if(cookies == null) return null;
		
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				return c;
			}
		}
		
		return null;
	}
}

