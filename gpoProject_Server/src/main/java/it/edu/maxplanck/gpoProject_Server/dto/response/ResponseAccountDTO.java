package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public record ResponseAccountDTO(
	String username,
    String description,
	boolean isAdmin,
	LocalDate createdAt,
	String path
) {}
