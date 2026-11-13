package com.famicare.FamicareBackend.zones.domain.model.commands;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneRadius;

/**
 *
 * @param safeZoneId zone to update
 * @param name       new name of the zone
 * @param center     new center of the zone
 * @param radius     new radius of the zone
 */
public record UpdateSafeZoneCommand(
        Long safeZoneId,
        ZoneName name,
        Coordinates center,
        ZoneRadius radius
) {

    public UpdateSafeZoneCommand {
        if (safeZoneId == null || safeZoneId <= 0)
            throw new IllegalArgumentException("zone.error.id.invalid");
        if (name == null)
            throw new IllegalArgumentException("zone.error.name.notBlank");
        if (center == null)
            throw new IllegalArgumentException("zone.error.center.required");
        if (radius == null)
            throw new IllegalArgumentException("zone.error.radius.range");
    }
}
