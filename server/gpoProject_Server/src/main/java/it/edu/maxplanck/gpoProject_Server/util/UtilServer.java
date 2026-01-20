package it.edu.maxplanck.gpoProject_Server.util;

public final class UtilServer {
	
	
	public static final String accessTokenSubjectPath = "${jwt.Subject.Token.Access}";
	public static final String accessCookiePath = "${servlet.Name.Cookie.Access}";
	public static final int timeExpirationDateAccessCookie = (int) (UtilToken.timeExpirationDateAccessToken / 1000);
	
	public static final String refreshTokenSubjectPath = "${jwt.Subject.Token.Refresh}";
	public static final String refreshCookiePath = "${servlet.Name.Cookie.Refresh}";
	public static final int timeExpirationDateRefreshCookie = (int) (UtilToken.timeExpirationDateRefreshToken / 1000);
}
