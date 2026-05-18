package it.edu.maxplanck.gpoProject_Server.dto.response;

import jakarta.servlet.http.Cookie;

public record ResponseAuth(
	Cookie access,
	Integer id
) {}
