package it.edu.maxplanck.gpoProject_Server_WS.model;

public class ModelCookies {

	public static final String accessCookiePath = "${j.Name.Cookie.Access}";
	public static final int accessCookie_TimeToLive = (int) (ModelTokens.accessToken_TimeToLive / 1000);
	
	public static final String refreshCookiePath = "${j.Name.Cookie.Refresh}";
	public static final int refreshCookie_TimeToLive = (int) (ModelTokens.refreshToken_TimeToLive / 1000);
}
