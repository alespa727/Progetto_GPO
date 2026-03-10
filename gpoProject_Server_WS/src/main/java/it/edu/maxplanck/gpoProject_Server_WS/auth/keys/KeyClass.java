package it.edu.maxplanck.gpoProject_Server_WS.auth.keys;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class KeyClass {

	private final Key key;
	private final long expiration;
	private final SignatureAlgorithm algorithm;
	
	public KeyClass(Key key, long expiration, SignatureAlgorithm algorithm) {
		super();
		this.key = key;
		this.expiration = expiration;
		this.algorithm = algorithm;
	}

	public KeyClass(
			String secret,
			Charset charset, 
			long expiration,
			SignatureAlgorithm algorithm
	) {
		super();
		
		if(charset == null) charset = StandardCharsets.UTF_8;
		this.key = Keys.hmacShaKeyFor(secret.getBytes(charset));
		
		this.expiration = expiration;
		
		if(algorithm == null) this.algorithm = algorithm;
		else this.algorithm = SignatureAlgorithm.HS256;
	}

	public KeyClass(
			String secret,
			long expiration
	) {
		super();
		
		Charset charset = StandardCharsets.UTF_8;
		this.key = Keys.hmacShaKeyFor(secret.getBytes(charset));
		
		this.expiration = expiration;
		
		this.algorithm = SignatureAlgorithm.HS256;
	}
	
	public Key getKey() {
		return key;
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public SignatureAlgorithm getAlgorithm() {
		return algorithm;
	}
}
