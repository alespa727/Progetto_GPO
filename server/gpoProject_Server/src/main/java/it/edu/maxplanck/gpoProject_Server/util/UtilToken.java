package it.edu.maxplanck.gpoProject_Server.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.SignatureAlgorithm;

public final class UtilToken {
	
	public static final String accessTokenPath = "${jwt.Secret.Token.Access}";
	public static final long timeExpirationDateAccessToken = 15 * 60 * 1000; // 15 min
	
	public static final String refreshTokenPath = "${jwt.Secret.Token.Refresh}";
	public static final long timeExpirationDateRefreshToken = 90L * 24 * 60 * 60 * 1000; // 90 giorni
	
	public static final Charset charset = StandardCharsets.UTF_8;
	
	public static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
}
