package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestStatus extends Request {
	private final boolean isOnline;

	public RequestStatus(boolean isOnline) {
		super();
		this.isOnline = isOnline;
	}

	public boolean isOnline() {
		return isOnline;
	}
}
