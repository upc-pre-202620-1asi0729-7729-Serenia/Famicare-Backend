package com.famicare.FamicareBackend.iot.domain.model.events;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.TelemetryEventType;

import java.time.Instant;

/**
 * Domain event raised when a paired device sends telemetry. Other bounded contexts
 * (Monitor, Zones) can react to it to store locations, evaluate safe zones or raise
 * risk monitor (panic, low battery, inactivity) without the IoT context knowing about them.
 */
public record TelemetryReceivedEvent(
        Long deviceId,
        ElderlyProfileId elderlyProfileId,
        TelemetryEventType eventType,
        Coordinates coordinates,
        BatteryLevel batteryLevel,
        Instant recordedAt,
        Instant receivedAt
) {
}
