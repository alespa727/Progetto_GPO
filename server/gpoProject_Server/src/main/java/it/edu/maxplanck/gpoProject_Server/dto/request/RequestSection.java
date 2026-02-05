package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestSection extends Request {
	private final String name;

	public RequestSection(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
