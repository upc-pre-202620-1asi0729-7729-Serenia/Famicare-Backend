package com.famicare.FamicareBackend.care.domain.model.events;

import java.time.Instant;

public record CaregiverAccessRevokedEvent(
        Long caregiverId,
        Long elderlyProfileId,
        Instant occurredAt
) {}