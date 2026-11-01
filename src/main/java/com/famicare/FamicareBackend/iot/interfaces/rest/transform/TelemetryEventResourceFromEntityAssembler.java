package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.TelemetryEventResource;

/**
 * Interface layer translator converting a {@link TelemetryEvent} aggregate to a {@link TelemetryEventResource}.
 */
public class TelemetryEventResourceFromEntityAssembler {
    public static TelemetryEventResource toResourceFromEntity(TelemetryEvent entity) {
        var coordinates = entity.getCoordinates();
        return new TelemetryEventResource(
                entity.getId(),
                entity.getDeviceId(),
                entity.getEventType().name(),
                coordinates == null ? null : coordinates.getLatitude(),
                coordinates == null ? null : coordinates.getLongitude(),
                entity.getBatteryLevel() == null ? null : entity.getBatteryLevel().value(),
                entity.getRecordedAt(),
                entity.getReceivedAt());
    }
}
