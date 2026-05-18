package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;

public record ResponseCallChatDTO(
	Integer id,
	LocalDateTime startTime,
	LocalDateTime endTime
) {}
