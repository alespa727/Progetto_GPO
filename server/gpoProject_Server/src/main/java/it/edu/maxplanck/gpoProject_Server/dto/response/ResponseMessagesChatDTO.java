package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public record ResponseMessagesChatDTO(
	Integer chatId,
	List<ResponseMessageChatDTO> messages
) {}
