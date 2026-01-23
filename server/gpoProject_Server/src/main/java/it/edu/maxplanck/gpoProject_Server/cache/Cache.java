package it.edu.maxplanck.gpoProject_Server.cache;

import java.util.HashSet;

public class Cache {

	private static final HashSet<Integer> setUserOnline = new HashSet<Integer>();

	public static HashSet<Integer> getSetuseronline() {
		return setUserOnline;
	}
}
