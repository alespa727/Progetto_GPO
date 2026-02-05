package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseMessagesChat extends Response {
	private final Integer chatId;
	private final List<ResponseMessageChat> messages;

	public ResponseMessagesChat(Integer chatId, List<ResponseMessageChat> messages) {
		super();
		this.chatId = chatId;
		this.messages = messages;
	}

	public Integer getChatId() {
		return chatId;
	}

	public List<ResponseMessageChat> getMessages() {
		return messages;
	}
}
