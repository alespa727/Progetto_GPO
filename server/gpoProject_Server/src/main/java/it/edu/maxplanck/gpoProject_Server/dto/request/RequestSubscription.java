package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestSubscription extends Request {
	private final String inviteCode;

	public RequestSubscription(String inviteCode) {
		super();
		this.inviteCode = inviteCode;
	}

	public String getInviteCode() {
		return inviteCode;
	}
}
