package it.edu.maxplanck.gpoProject_Server.dto.response;

public class ResponseMessageChannelSectionCommunity extends Response {
	private final Integer id;
	private final String message;

	public ResponseMessageChannelSectionCommunity(Integer id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
}
