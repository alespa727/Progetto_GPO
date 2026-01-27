package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public record ResponseCallsChatDTO(
	Integer chatId,
	List<ResponseCallChatDTO> calls
) {}
