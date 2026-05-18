package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;

public record ResponseChatDTO(
	Integer id,
    LocalDateTime lastMessage,
	ResponseFriendDTO friend
) {}
