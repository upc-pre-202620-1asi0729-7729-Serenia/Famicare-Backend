package com.famicare.FamicareBackend.iot.domain.model.commands;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.TelemetryEventType;

import java.time.Instant;

/**
 * Records a message sent by an IoT device (position, heartbeat, panic button or fall).
 * Coordinates and battery level are optional in general, but each event type
 * declares which of them it requires.
 *
 * @param deviceId     device that sent the message
 * @param eventType    kind of message
 * @param coordinates  GPS position, when the event carries one
 * @param batteryLevel battery percentage, when the event carries one
 * @param recordedAt   instant at which the device produced the reading
 */
public record RecordTelemetryCommand(
        Long deviceId,
        TelemetryEventType eventType,
        Coordinates coordinates,
        BatteryLevel batteryLevel,
        Instant recordedAt
) {

    public RecordTelemetryCommand {
        if (deviceId == null || deviceId <= 0)
            throw new IllegalArgumentException("device.error.id.invalid");
        if (eventType == null)
            throw new IllegalArgumentException("telemetry.error.eventType.invalid");
        if (recordedAt == null)
            throw new IllegalArgumentException("telemetry.error.recordedAt.required");
        if (eventType.requiresCoordinates() && coordinates == null)
            throw new IllegalArgumentException("telemetry.error.coordinates.required");
        if (eventType.requiresBatteryLevel() && batteryLevel == null)
            throw new IllegalArgumentException("telemetry.error.batteryLevel.required");
    }
}
