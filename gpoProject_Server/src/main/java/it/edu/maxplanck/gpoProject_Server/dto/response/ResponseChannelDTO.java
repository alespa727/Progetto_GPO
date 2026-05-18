package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public record ResponseChannelDTO(
	Integer id,
	String name,
	String type,
	String description,
	LocalDate createdAt
) {}
