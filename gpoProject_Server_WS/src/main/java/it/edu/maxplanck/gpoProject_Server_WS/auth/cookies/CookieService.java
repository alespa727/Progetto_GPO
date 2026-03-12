package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.edu.maxplanck.gpoProject_Server_WS.model.ModelCookies;

@Service
public class CookieService {

	private final CookieClass accessCookie;
	private final CookieClass refreshCookie;
	private final CookieManager cookieManager;

	public CookieService(
			@Value(ModelCookies.accessCookiePath) String accessCookieName,
			@Value(ModelCookies.refreshCookiePath) String refreshCookieName, 
			CookieManager cookieManager
	) {
		super();
		this.accessCookie = new CookieClass(accessCookieName, ModelCookies.accessCookie_TimeToLive);
		this.refreshCookie = new CookieClass(accessCookieName, ModelCookies.refreshCookie_TimeToLive);
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

}
