package it.edu.maxplanck.gpoProject_Server_WS.model;

public class ModelTokens {
	
	public static final String accessTokenPath = "${jwt.Secret.Token.Access}";
	public static final long accessToken_TimeToLive = 15 * 60 * 1000; // 15 min
	
	public static final String refreshTokenPath = "${jwt.Secret.Token.Refresh}";
	public static final long refreshToken_TimeToLive = 90L * 24 * 60 * 60 * 1000; // 90 giorni
}
