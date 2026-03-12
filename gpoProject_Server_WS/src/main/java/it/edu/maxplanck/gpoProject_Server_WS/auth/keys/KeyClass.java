package it.edu.maxplanck.gpoProject_Server_WS.auth.keys;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class KeyClass {

	private final Key key;
	private final long keyTimeToLive;
	private final SignatureAlgorithm algorithm;
	
	public KeyClass(Key key, long keyTimeToLive, SignatureAlgorithm algorithm) {
		super();
		this.key = key;
		this.keyTimeToLive = keyTimeToLive;
		this.algorithm = algorithm;
	}

	public KeyClass(
			String secret,
			Charset charset, 
			long keyTimeToLive,
			SignatureAlgorithm algorithm
	) {
		super();
		
		if(charset == null) charset = StandardCharsets.UTF_8;
		this.key = Keys.hmacShaKeyFor(secret.getBytes(charset));
		
		this.keyTimeToLive = keyTimeToLive;
		
		if(algorithm == null) this.algorithm = algorithm;
		else this.algorithm = SignatureAlgorithm.HS256;
	}

	public KeyClass(
			String secret,
			long keyTimeToLive
	) {
		super();
		
		Charset charset = StandardCharsets.UTF_8;
		this.key = Keys.hmacShaKeyFor(secret.getBytes(charset));
		
		this.keyTimeToLive = keyTimeToLive;
		
		this.algorithm = SignatureAlgorithm.HS256;
	}
	
	public Key getKey() {
		return key;
	}
	
	public long getExpiration() {
		return keyTimeToLive;
	}
	
	public SignatureAlgorithm getAlgorithm() {
		return algorithm;
	}
}
