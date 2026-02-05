package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestMessageChat extends Request {
	private final String message;
	
	public RequestMessageChat(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
