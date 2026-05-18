package it.edu.maxplanck.gpoProject_Server.dto.request;

import it.edu.maxplanck.gpoProject_Server.database.model.ModelDataDatabase;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestAccount(
      Integer id,
      String username,
      String imagePath
) {
}
