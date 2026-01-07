package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.Instant;

public record ResponseUserDTO(

		String username,
		Instant createdAt
) {}
