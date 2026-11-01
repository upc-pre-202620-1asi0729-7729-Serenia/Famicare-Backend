package com.famicare.FamicareBackend.iot.domain.model.queries;

import java.time.Instant;

/**
 * Telemetry events of a device whose {@code recordedAt} lies in {@code [from, to]}.
 */
public record GetTelemetryEventsByDeviceIdAndPeriodQuery(Long deviceId, Instant from, Instant to) {
    public GetTelemetryEventsByDeviceIdAndPeriodQuery {
        if (deviceId == null || deviceId <= 0)
            throw new IllegalArgumentException("device.error.id.invalid");
        if (from == null || to == null || from.isAfter(to))
            throw new IllegalArgumentException("telemetry.error.period.invalid");
    }
}
