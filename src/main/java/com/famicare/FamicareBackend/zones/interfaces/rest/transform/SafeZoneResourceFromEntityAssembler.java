package com.famicare.FamicareBackend.zones.interfaces.rest.transform;

import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.SafeZoneResource;

/**
 * Interface layer translator converting a {@link SafeZone} aggregate to a {@link SafeZoneResource}.
 */
public class SafeZoneResourceFromEntityAssembler {
    public static SafeZoneResource toResourceFromEntity(SafeZone entity) {
        return new SafeZoneResource(
                entity.getId(),
                entity.getElderlyProfileId().value(),
                entity.getName().value(),
                entity.getCenter().getLatitude(),
                entity.getCenter().getLongitude(),
                entity.getRadius().value(),
                entity.getPresence().name(),
                entity.getLastEvaluatedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
