package com.famicare.FamicareBackend.care.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateElderlyProfileResource(
        @Schema(example = "Elena Martinez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 150)
        String fullName,

        @Schema(example = "78", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Min(40)
        @Max(120)
        Integer age,

        @Schema(example = "Hipertension controlada, sale a caminar por las mañanas")
        @Size(max = 500)
        String medicalNotes
) {}