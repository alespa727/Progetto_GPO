package it.edu.maxplanck.gpoProject_Server_WS.auth.cookies;

public class CookieClass {

	private final String cookieName;
	private final int cookieTimeToLive;
	private final boolean isHttpOnly;
	private final boolean isSecure;
	private final String path;

	public CookieClass(String cookieName, int cookieTimeToLive, boolean isHttpOnly, boolean isSecure, String path) {
		super();
		this.cookieName = cookieName;
		this.cookieTimeToLive = cookieTimeToLive;
		this.isHttpOnly = isHttpOnly;
		this.isSecure = isSecure;
		this.path = path;
	}

	public String getCookieName() {
		return cookieName;
	}

	public int getCookieTimeToLive() {
		return cookieTimeToLive;
	}

	public boolean isHttpOnly() {
		return isHttpOnly;
	}

	public boolean isSecure() {
		return isSecure;
	}

	public String getPath() {
		return path;
	}
}
