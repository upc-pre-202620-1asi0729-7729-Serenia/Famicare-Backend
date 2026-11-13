package com.famicare.FamicareBackend.zones.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response resource describing a safe zone.
 * {@code presence} tells where the last known coordinate stands: UNKNOWN, INSIDE or OUTSIDE.
 */
public record SafeZoneResource(
        Long id,
        Long elderlyProfileId,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer radiusMeters,
        String presence,
        Instant lastEvaluatedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
