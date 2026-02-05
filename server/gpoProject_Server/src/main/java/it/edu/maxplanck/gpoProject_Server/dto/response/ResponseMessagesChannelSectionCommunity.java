package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseMessagesChannelSectionCommunity extends Response {
	private final List<ResponseMessageChannelSectionCommunity> messages;

	public ResponseMessagesChannelSectionCommunity(List<ResponseMessageChannelSectionCommunity> messages) {
		super();
		this.messages = messages;
	}

	public List<ResponseMessageChannelSectionCommunity> getMessages() {
		return messages;
	}
}
