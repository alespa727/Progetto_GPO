package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestChannel extends Request {
	private final String name;
	private final String type;
	private final String description;

	public RequestChannel(String name, String type, String description) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}
}
