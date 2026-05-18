package it.edu.maxplanck.gpoProject_Server.dto.response;

public record ResponseAttachedChatDTO(
	Integer id,
	String path,
    String originalname,
	String filename,
	String extension
) {}
