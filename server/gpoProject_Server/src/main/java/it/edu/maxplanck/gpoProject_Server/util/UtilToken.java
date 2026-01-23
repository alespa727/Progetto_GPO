package it.edu.maxplanck.gpoProject_Server.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.SignatureAlgorithm;

public final class UtilToken {
	
	// Path per ottenere il valore per generare la password per criptare il token di accesso
	public static final String accessTokenPath = "${jwt.Secret.Token.Access}";
	
	// Valore temporale il quale serve per decidere se il token di accesso e' scaduto o no
	public static final long timeExpirationDateAccessToken = 15 * 60 * 1000; // 15 min
	
	
	// Path per ottenere il valore per generare la password per criptare il token di refresh
	public static final String refreshTokenPath = "${jwt.Secret.Token.Refresh}";
	
	// Valore temporale il quale serve per decidere se il token di refresh e' scaduto o no
	public static final long timeExpirationDateRefreshToken = 90L * 24 * 60 * 60 * 1000; // 90 giorni
	
	// Charset usato
	public static final Charset charset = StandardCharsets.UTF_8;
	
	// Algoritmo di criptazione
	public static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
}
