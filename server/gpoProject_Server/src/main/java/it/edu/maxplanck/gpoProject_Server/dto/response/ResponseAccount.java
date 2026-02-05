package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public class ResponseAccount extends Response {
	private final String username;
	private final boolean isAdmin;
	private final LocalDate createdAt;
	private final String path;
	
	public ResponseAccount(String username, boolean isAdmin, LocalDate createdAt, String path) {
		super();
		this.username = username;
		this.isAdmin = isAdmin;
		this.createdAt = createdAt;
		this.path = path;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public String getPath() {
		return path;
	}
}
