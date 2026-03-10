package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

import org.springframework.stereotype.Service;

@Service
public class CookieService {

	private final CookieManager cookieManager;

	public CookieService(CookieManager cookieManager) {
		super();
		this.cookieManager = cookieManager;
	}

	public CookieManager getCookieManager() {
		return cookieManager;
	}
}
