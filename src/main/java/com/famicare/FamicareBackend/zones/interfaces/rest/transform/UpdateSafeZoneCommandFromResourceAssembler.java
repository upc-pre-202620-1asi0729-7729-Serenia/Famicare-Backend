package com.famicare.FamicareBackend.zones.interfaces.rest.transform;

import com.famicare.FamicareBackend.zones.domain.model.commands.UpdateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneRadius;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.UpdateSafeZoneResource;

/**
 * Interface layer translator converting an {@link UpdateSafeZoneResource} (plus the zone id
 * taken from the URL) to an {@link UpdateSafeZoneCommand}.
 */
public class UpdateSafeZoneCommandFromResourceAssembler {
    public static UpdateSafeZoneCommand toCommandFromResource(Long safeZoneId, UpdateSafeZoneResource resource) {
        return new UpdateSafeZoneCommand(
                safeZoneId,
                new ZoneName(resource.name()),
                new Coordinates(resource.latitude(), resource.longitude()),
                new ZoneRadius(resource.radiusMeters()));
    }
}
