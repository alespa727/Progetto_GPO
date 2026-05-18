package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseMessageChatDTO(
	Integer messageId,
	String username,
	String message,
	LocalDateTime sentAt,
	List<ResponseAttachedChatDTO> attachments
) {}
