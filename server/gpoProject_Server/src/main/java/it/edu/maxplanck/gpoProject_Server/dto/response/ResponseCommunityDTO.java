package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ResponseCommunityDTO(
        Integer id,
	String name,
	String inviteCode,
	boolean isInviteCodeValid,
	String description,
	LocalDate createdAt,
	List<ResponseSectionDTO> sections
) {}
