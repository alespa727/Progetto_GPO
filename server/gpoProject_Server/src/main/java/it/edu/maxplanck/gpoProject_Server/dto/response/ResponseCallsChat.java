package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseCallsChat extends Response {
	private final Integer chatId;
	private final List<ResponseCallChat> calls;

	public ResponseCallsChat(Integer chatId, List<ResponseCallChat> calls) {
		super();
		this.chatId = chatId;
		this.calls = calls;
	}

	public Integer getChatId() {
		return chatId;
	}

	public List<ResponseCallChat> getCalls() {
		return calls;
	}
}
