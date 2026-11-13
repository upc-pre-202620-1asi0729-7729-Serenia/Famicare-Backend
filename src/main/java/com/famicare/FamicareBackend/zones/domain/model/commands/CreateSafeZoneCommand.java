package com.famicare.FamicareBackend.zones.domain.model.commands;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneRadius;

/**
 * Defines a circular safe zone around a point for an elderly profile (US-10).
 *
 * @param elderlyProfileId elderly profile the zone belongs to
 * @param name             name of the zone, unique per elderly profile
 * @param center           center of the zone
 * @param radius           radius of the zone
 */
public record CreateSafeZoneCommand(
        ElderlyProfileId elderlyProfileId,
        ZoneName name,
        Coordinates center,
        ZoneRadius radius
) {

    public CreateSafeZoneCommand {
        if (elderlyProfileId == null)
            throw new IllegalArgumentException("zone.error.elderlyProfileId.invalid");
        if (name == null)
            throw new IllegalArgumentException("zone.error.name.notBlank");
        if (center == null)
            throw new IllegalArgumentException("zone.error.center.required");
        if (radius == null)
            throw new IllegalArgumentException("zone.error.radius.range");
    }
}
