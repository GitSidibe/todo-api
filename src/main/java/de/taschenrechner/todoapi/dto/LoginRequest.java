package de.taschenrechner.todoapi.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	  @NotBlank(message = "Username darf nicht leer sein")
      String username,

      @NotBlank(message = "Passwort darf nicht leer sein")
      String password
      )
{
}
