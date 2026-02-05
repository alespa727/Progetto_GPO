package it.edu.maxplanck.gpoProject_Server.dto.response;

public class ResponseChat extends Response {
	private final Integer id;
	private final ResponseFriend friend;

	public ResponseChat(Integer id, ResponseFriend friend) {
		super();
		this.id = id;
		this.friend = friend;
	}

	public Integer getId() {
		return id;
	}

	public ResponseFriend getFriend() {
		return friend;
	}
}
