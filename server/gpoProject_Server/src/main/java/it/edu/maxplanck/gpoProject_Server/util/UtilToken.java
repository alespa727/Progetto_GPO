package it.edu.maxplanck.gpoProject_Server.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.SignatureAlgorithm;

public final class UtilToken {
	
	public static final String accessTokenPath = "${jwt.Secret.Token.Access}";
	public static final String accessTokenExpirationDatePath = "${jwt.ExpirationDate.Token.Access}";
	public static final long timeExpirationDateAccessToken = 1000L * 60 * 60 * 24;
	
	public static final String refreshTokenPath = "${jwt.Secret.Token.Refresh}";
	public static final String refreshTokenExpirationDatePath = "${jwt.ExpirationDate.Token.Refresh}";
	public static final long timeExpirationDateRefreshToken = 1000L * 60 * 60 * 24;
	
	public static final Charset charset = StandardCharsets.UTF_8;
	
	public static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
}
