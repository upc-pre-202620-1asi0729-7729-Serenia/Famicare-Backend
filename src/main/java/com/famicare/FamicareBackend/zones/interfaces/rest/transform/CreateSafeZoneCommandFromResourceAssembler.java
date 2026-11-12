package com.famicare.FamicareBackend.zones.interfaces.rest.transform;

import com.famicare.FamicareBackend.zones.domain.model.commands.CreateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneRadius;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.CreateSafeZoneResource;

/**
 * Interface layer translator converting a {@link CreateSafeZoneResource} to a {@link CreateSafeZoneCommand}.
 */
public class CreateSafeZoneCommandFromResourceAssembler {
    public static CreateSafeZoneCommand toCommandFromResource(CreateSafeZoneResource resource) {
        return new CreateSafeZoneCommand(
                new ElderlyProfileId(resource.elderlyProfileId()),
                new ZoneName(resource.name()),
                new Coordinates(resource.latitude(), resource.longitude()),
                new ZoneRadius(resource.radiusMeters()));
    }
}
