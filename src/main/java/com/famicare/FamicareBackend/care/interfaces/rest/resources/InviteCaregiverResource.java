package com.famicare.FamicareBackend.care.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record InviteCaregiverResource(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Positive
        Long elderlyProfileId,

        @Schema(example = "Lucía Santos", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 150)
        String fullName,

        @Schema(example = "lucia.santos@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @Schema(example = "COLLABORATOR", allowableValues = {"ADMINISTRATOR", "COLLABORATOR"})
        String role,

        @Schema(example = "Enfermera - Alertas y ubicación")
        @Size(max = 120)
        String relationshipLabel,

        @Schema(example = "Lun—Vie - 09:00—18:00")
        @Size(max = 100)
        String schedule
) {}