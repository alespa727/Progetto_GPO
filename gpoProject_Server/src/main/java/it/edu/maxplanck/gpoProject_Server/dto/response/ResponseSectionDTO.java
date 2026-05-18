package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public record ResponseSectionDTO(
	Integer id,
	String name,
	List<ResponseChannelDTO> channels
) {}
