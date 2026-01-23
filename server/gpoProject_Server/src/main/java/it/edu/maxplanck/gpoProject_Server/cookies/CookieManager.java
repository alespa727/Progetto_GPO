package it.edu.maxplanck.gpoProject_Server.cookies;

import java.util.ArrayList;
import java.util.HashMap;

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

	// Mappa di cookies richiesti
	@Override
	public HashMap<String, Cookie> findCookies(HttpServletRequest request, ArrayList<String> cookiesName) {
		// TODO Auto-generated method stub
		
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		
		HashMap<String, Cookie> mapCookies = new HashMap<String, Cookie>();
		for (Cookie c : cookies) {
			
			boolean cFind = false;
			for(int i = 0; i < cookiesName.size() && !cFind; i++) {
				
				if(c.getName().equals(cookiesName.get(i))) {
					mapCookies.put(c.getName(), c);
					cookiesName.remove(i);
				}
			}
		}
		
		if(mapCookies.size() == 0) return null;
		return mapCookies;
	}

	// Aggiorna i dati di un cookie
	@Override
	public void updateCookie(Cookie cookie, String value, Boolean httpOnly, Boolean secure, String path, Integer maxAge) {
		// TODO Auto-generated method stub
		
		if(cookie == null) return;
		
		if(value != null) cookie.setValue(value);
		if(httpOnly != null) cookie.setHttpOnly(httpOnly);
		if(secure != null) cookie.setSecure(secure);
		if(path != null) cookie.setPath(path);
		if(maxAge != null) cookie.setMaxAge(maxAge);
		
	}
	
	// Controlla se un cookie e' valido (cookie rimandato al server con maxAge sempre = -1)
	@Override
	public boolean isCookieValid(Cookie cookie) {
		// TODO Auto-generated method stub
	    return cookie != null && cookie.getValue() != null && !cookie.getValue().isBlank();
	}
	
	// Elimina un cookie
	@Override
	public void removeCookie(Cookie cookie, String path) {
		// TODO Auto-generated method stub
		
		if(cookie == null) return;
		cookie.setValue("");
        cookie.setPath(path);
        cookie.setMaxAge(0);
	}
}

