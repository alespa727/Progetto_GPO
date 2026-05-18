package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseMessageCommunityDTO(
        Integer messageId,
        String username,
        String message,
        LocalDateTime sentAt
) {}

