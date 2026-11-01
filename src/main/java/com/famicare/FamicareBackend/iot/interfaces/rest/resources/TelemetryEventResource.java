package com.famicare.FamicareBackend.iot.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response resource describing a stored telemetry event.
 */
public record TelemetryEventResource(
        Long id,
        Long deviceId,
        String eventType,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer batteryLevel,
        Instant recordedAt,
        Instant receivedAt
) {
}
