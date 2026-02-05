package it.edu.maxplanck.gpoProject_Server.dto.response;

public class ResponseFriend extends Response {
	private final String username;
	private final String imagePath;

	public ResponseFriend(String username, String imagePath) {
		super();
		this.username = username;
		this.imagePath = imagePath;
	}

	public String getUsername() {
		return username;
	}

	public String getImagePath() {
		return imagePath;
	}
}
