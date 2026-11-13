package de.taschenrechner.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateDTO(
        @NotBlank(message = "Titel darf nicht leer sein")
        @Size(max = 100, message = "Titel darf maximal 100 Zeichen haben")
        String titel,

        boolean erledigt
) {
}