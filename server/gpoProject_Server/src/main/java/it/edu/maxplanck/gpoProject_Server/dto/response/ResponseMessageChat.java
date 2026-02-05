package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseMessageChat extends Response {
	private final Integer messageId;
	private final String username;
	private final String message;
	private final LocalDateTime sentAt;
	private final List<ResponseAttachedChat> attachments;

	public ResponseMessageChat(Integer messageId, String username, String message, LocalDateTime sentAt, List<ResponseAttachedChat> attachments) {
		super();
		this.messageId = messageId;
		this.username = username;
		this.message = message;
		this.sentAt = sentAt;
		this.attachments = attachments;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public List<ResponseAttachedChat> getAttachments() {
		return attachments;
	}
}
