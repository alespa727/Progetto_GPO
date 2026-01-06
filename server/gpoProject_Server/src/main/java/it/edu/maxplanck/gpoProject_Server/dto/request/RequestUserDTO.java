package it.edu.maxplanck.gpoProject_Server.dto.request;

import it.edu.maxplanck.gpoProject_Server.annotations.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUserDTO(

        @NotBlank(message = "Username obbligatorio")
        @Size(max = 20, message = "Username max 20 caratteri")
        String username,

        @NotBlank(message = "Password obbligatoria")
        @StrongPassword
        @Size(max = 256, message = "Password max 256 caratteri")
        String password,

        boolean isAdmin,

        @Size(max = 100, message = "image path max 100 caratteri")
        String imagePath
) {}
