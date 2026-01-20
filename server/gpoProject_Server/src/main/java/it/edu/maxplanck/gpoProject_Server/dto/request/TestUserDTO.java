package it.edu.maxplanck.gpoProject_Server.dto.request;

public record TestUserDTO(
	String username,
	String password,
	Boolean isAdmin,
	String imagePath
) {}
