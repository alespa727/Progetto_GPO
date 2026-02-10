package it.edu.maxplanck.gpoProject_Server.dto.request;

public record RequestCommunityDTO(
	boolean isInviteCodeValid,
	String name,
	String description
) {}
