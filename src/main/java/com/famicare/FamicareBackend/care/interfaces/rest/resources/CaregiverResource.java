package com.famicare.FamicareBackend.care.interfaces.rest.resources;

import java.time.Instant;

public record CaregiverResource(
        Long id,
        Long elderlyProfileId,
        String initials,
        String name,
        String email,
        String roleType,
        String role,
        String availability,
        String schedule,
        String status,
        Instant invitedAt
) {}