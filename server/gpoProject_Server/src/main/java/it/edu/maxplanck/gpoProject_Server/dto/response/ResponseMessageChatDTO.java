package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;

public record ResponseMessageChatDTO(
	Integer messageId,
	String username,
	String message,
	LocalDateTime sentAt
) {}
