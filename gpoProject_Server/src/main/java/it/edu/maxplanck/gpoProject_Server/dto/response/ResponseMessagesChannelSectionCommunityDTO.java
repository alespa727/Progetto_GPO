package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public record ResponseMessagesChannelSectionCommunityDTO(
	List<ResponseMessageChannelSectionCommunityDTO> messages
) {}
