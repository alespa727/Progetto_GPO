package it.edu.maxplanck.gpoProject_Server.dto.response;

public class ResponseProfile extends Response {
	private final String imagePath;

	public ResponseProfile(String imagePath) {
		super();
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}
}
