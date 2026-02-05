package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestCommunity extends Request {
	private final boolean isInviteCodeValid;
	private final String name;
	private final String description;

	public RequestCommunity(boolean isInviteCodeValid, String name, String description) {
		super();
		this.isInviteCodeValid = isInviteCodeValid;
		this.name = name;
		this.description = description;
	}

	public boolean isInviteCodeValid() {
		return isInviteCodeValid;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
