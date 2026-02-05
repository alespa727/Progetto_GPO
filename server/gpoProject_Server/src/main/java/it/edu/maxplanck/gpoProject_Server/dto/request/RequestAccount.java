package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestAccount extends Request {
	private final String username;
	private final String password;

	public RequestAccount(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
