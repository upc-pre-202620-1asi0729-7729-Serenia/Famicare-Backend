package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.commands.RecordTelemetryCommand;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.RecordTelemetryResource;

/**
 * Interface layer translator converting a {@link RecordTelemetryResource} (plus the device id
 * taken from the URL) to a {@link RecordTelemetryCommand}.
 */
public class RecordTelemetryCommandFromResourceAssembler {
    public static RecordTelemetryCommand toCommandFromResource(Long deviceId, RecordTelemetryResource resource) {
        Coordinates coordinates = null;
        if (resource.latitude() != null || resource.longitude() != null) {
            coordinates = new Coordinates(resource.latitude(), resource.longitude());
        }
        BatteryLevel batteryLevel = resource.batteryLevel() == null
                ? null
                : new BatteryLevel(resource.batteryLevel());
        return new RecordTelemetryCommand(
                deviceId,
                IotValueObjectFromStringAssembler.toTelemetryEventTypeFromString(resource.eventType()),
                coordinates,
                batteryLevel,
                resource.recordedAt());
    }
}
