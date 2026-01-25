package it.edu.maxplanck.gpoProject_Server.util;

import java.security.SecureRandom;

public class GenericUtil {
	
	public static String standardPathImages = "http://localhost:8080/images/";

	public static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public static String generateString(int length, String pattern) {
	    SecureRandom random = new SecureRandom();
	    StringBuilder sb = new StringBuilder(length);

	    for (int i = 0; i < length; i++) {
	        sb.append(pattern.charAt(random.nextInt(pattern.length())));
	    }
	    return sb.toString();
	}
}
