package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

public class CookieClass {

	private final String cookieName;
	private final int cookieTimeToLive;

	public CookieClass(String cookieName, int cookieTimeToLive) {
		super();
		this.cookieName = cookieName;
		this.cookieTimeToLive = cookieTimeToLive;
	}

	public String getCookieName() {
		return cookieName;
	}

	public int getCookieTimeToLive() {
		return cookieTimeToLive;
	}
}
