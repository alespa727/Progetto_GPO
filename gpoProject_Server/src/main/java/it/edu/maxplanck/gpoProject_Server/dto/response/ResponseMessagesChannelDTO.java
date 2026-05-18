package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public record ResponseMessagesChannelDTO(
	Integer channelId,
	List<ResponseMessageChatDTO> messages
) {}
