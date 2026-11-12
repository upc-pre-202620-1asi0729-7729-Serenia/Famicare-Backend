package com.famicare.FamicareBackend.zones.domain.model.events;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;

import java.time.Instant;

/**
 * Domain event raised when the last known coordinate of an elderly person moves from outside
 * to inside a safe zone.
 */
public record SafeZoneEnteredEvent(
        Long safeZoneId,
        ElderlyProfileId elderlyProfileId,
        String zoneName,
        Coordinates position,
        Instant recordedAt
) {
}
