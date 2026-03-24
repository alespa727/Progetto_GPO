package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.edu.maxplanck.gpoProject_Server_WS.model.ModelCookies;
import jakarta.servlet.http.Cookie;

@Service
public class CookieService {

	private final CookieClass accessCookie;
	private final CookieClass refreshCookie;
	private final CookieManager cookieManager;

	public CookieService(
			@Value(ModelCookies.accessCookieNamePath) String accessCookieName,
			@Value(ModelCookies.refreshCookieNamePath) String refreshCookieName, 
			CookieManager cookieManager
	) {
		super();
		this.accessCookie = new CookieClass(accessCookieName, ModelCookies.accessCookie_TimeToLive, true, false, ModelCookies.cookiesPath);
		this.refreshCookie = new CookieClass(accessCookieName, ModelCookies.refreshCookie_TimeToLive, true, false, ModelCookies.cookiesPath);
		this.cookieManager = cookieManager;
	}

	public CookieClass getAccessCookie() {
		return accessCookie;
	}

	public CookieClass getRefreshCookie() {
		return refreshCookie;
	}

	public CookieManager getCookieManager() {
		return cookieManager;
	}
	
	// Access Cookie
	public Cookie generate_AccessCookie(String value) {
		return this.cookieManager.generateCookie(
				this.accessCookie.getCookieName(), 
				value, 
				this.accessCookie.isHttpOnly(), 
				this.accessCookie.isSecure(), 
				this.accessCookie.getPath(), 
				this.accessCookie.getCookieTimeToLive()
		);
	}
	
	// Refresh Cookie
	public Cookie generate_RefreshCookie(String value) {
		return this.cookieManager.generateCookie(
				this.refreshCookie.getCookieName(), 
				value, 
				this.refreshCookie.isHttpOnly(), 
				this.refreshCookie.isSecure(), 
				this.refreshCookie.getPath(), 
				this.refreshCookie.getCookieTimeToLive()
		);
	}
}
