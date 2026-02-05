package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestMessageCommunity extends Request {
	private final String message;

	public RequestMessageCommunity(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
