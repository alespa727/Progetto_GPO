package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public record ResponseAccountDTO(
	String username,
	boolean isAdmin,
	LocalDate createdAt,
	String path
) {}
