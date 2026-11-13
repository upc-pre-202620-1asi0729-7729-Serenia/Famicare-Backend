package com.famicare.FamicareBackend.zones.domain.model.commands;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;

import java.time.Instant;

/**
 * Evaluates a newly reported position of an elderly person against all of their safe zones (US-12).
 *
 * @param elderlyProfileId elderly profile that was located
 * @param coordinates      reported position
 * @param recordedAt       instant at which the device produced the position
 */
public record EvaluateLocationCommand(ElderlyProfileId elderlyProfileId, Coordinates coordinates, Instant recordedAt) {

    public EvaluateLocationCommand {
        if (elderlyProfileId == null)
            throw new IllegalArgumentException("zone.error.elderlyProfileId.invalid");
        if (coordinates == null)
            throw new IllegalArgumentException("zone.error.center.required");
        if (recordedAt == null)
            throw new IllegalArgumentException("telemetry.error.recordedAt.required");
    }
}
