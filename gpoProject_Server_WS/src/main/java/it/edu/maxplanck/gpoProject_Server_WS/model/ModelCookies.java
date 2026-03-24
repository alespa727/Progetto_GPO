package it.edu.maxplanck.gpoProject_Server_WS.model;

public class ModelCookies {

	public static final String accessCookieNamePath = "${j.Name.Cookie.Access:#{Access_Cookie}}";
	public static final int accessCookie_TimeToLive = (int) (ModelTokens.accessToken_TimeToLive / 1000);
	
	public static final String refreshCookieNamePath = "${j.Name.Cookie.Refresh:#{Refresh_Cookie}}";
	public static final int refreshCookie_TimeToLive = (int) (ModelTokens.refreshToken_TimeToLive / 1000);
	
	public static final String cookiesPath = "${j.Path.Cookies}";
}
