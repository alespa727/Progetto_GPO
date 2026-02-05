package it.edu.maxplanck.gpoProject_Server.dto.request;

import java.util.List;

public class RequestFriends extends Request {
	private final List<RequestFriend> users;

	public RequestFriends(List<RequestFriend> users) {
		super();
		this.users = users;
	}

	public List<RequestFriend> getUsers() {
		return users;
	}
}
