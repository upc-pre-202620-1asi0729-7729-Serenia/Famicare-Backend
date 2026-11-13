package com.famicare.FamicareBackend.care.interfaces.rest.resources;

import java.time.Instant;

public record ElderlyProfileResource(
        Long id,
        String fullName,
        Integer age,
        String medicalNotes,
        Instant createdAt
) {}