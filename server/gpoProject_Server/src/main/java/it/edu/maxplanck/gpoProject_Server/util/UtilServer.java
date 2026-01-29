package it.edu.maxplanck.gpoProject_Server.util;

/**
 * Classe di utilita' per il Server
 */
public final class UtilServer {
	
	// Subject del token di accesso
	public static final String accessTokenSubject = "AccessToken";
	
	// Nome del cookie di accesso
	public static final String accessCookieName = "AccessCookie";
	
	// Valore di eliminazione del cookie di accesso, calcolato in base al token di accesso
	public static final int timeExpirationDateAccessCookie = (int) (UtilToken.timeExpirationDateAccessToken / 1000);
	
	
	// Subject del token di refresh
	public static final String refreshTokenSubject = "RefreshToken";
	
	// Nome del cookie di refresh
	public static final String refreshCookieName = "RefreshCookie";
	
	// Valore di eliminazione del cookie di refresh, calcolato in base al token di refresh
	public static final int timeExpirationDateRefreshCookie = (int) (UtilToken.timeExpirationDateRefreshToken / 1000);
}
