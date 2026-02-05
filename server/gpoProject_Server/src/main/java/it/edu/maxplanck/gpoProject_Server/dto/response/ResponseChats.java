package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseChats extends Response {
	private final List<ResponseChat> chats;

	public ResponseChats(List<ResponseChat> chats) {
		super();
		this.chats = chats;
	}

	public List<ResponseChat> getChats() {
		return chats;
	}
}
