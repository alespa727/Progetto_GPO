package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseFriends extends Response {
	private final List<ResponseFriend> friends;

	public ResponseFriends(List<ResponseFriend> friends) {
		super();
		this.friends = friends;
	}

	public List<ResponseFriend> getFriends() {
		return friends;
	}
}
