package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestFriend extends Request {
	private final String username;

	public RequestFriend(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
