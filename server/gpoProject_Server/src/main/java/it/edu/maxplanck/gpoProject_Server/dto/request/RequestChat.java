package it.edu.maxplanck.gpoProject_Server.dto.request;

public class RequestChat extends Request {
	private final RequestFriend friend;

	public RequestChat(RequestFriend friend) {
		super();
		this.friend = friend;
	}

	public RequestFriend getFriend() {
		return friend;
	}
}
