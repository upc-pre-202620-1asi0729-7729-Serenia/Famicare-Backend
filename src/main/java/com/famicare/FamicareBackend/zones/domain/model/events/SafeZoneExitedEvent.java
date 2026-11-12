package com.famicare.FamicareBackend.zones.domain.model.events;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;

import java.time.Instant;

/**
 * Domain event raised when the last known coordinate of an elderly person moves from inside
 * to outside a safe zone. The Monitor context reacts to it by raising a zone exit alert (US-12).
 */
public record SafeZoneExitedEvent(
        Long safeZoneId,
        ElderlyProfileId elderlyProfileId,
        String zoneName,
        Coordinates position,
        Instant recordedAt
) {
}
