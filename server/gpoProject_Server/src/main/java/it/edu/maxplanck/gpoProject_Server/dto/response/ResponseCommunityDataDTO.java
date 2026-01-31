package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public record ResponseCommunityDataDTO(
	String name,
	String inviteCode,
	boolean isInviteCodeValid,
	String description,
	LocalDate createdAt
) {}
