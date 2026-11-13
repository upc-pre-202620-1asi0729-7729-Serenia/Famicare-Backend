package com.famicare.FamicareBackend.care.domain.model.events;

import java.time.Instant;

public record CaregiverInvitedEvent(
        Long caregiverId,
        Long elderlyProfileId,
        String email,
        Instant occurredAt
) {}