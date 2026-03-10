package it.edu.maxplanck.gpoProject_Server.dto.request;

public record RequestCall(
        String caller,
        String called
) {}
