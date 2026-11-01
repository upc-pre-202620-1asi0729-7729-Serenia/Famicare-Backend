package com.famicare.FamicareBackend.iot.interfaces.rest.resources;

import java.time.Instant;

/**
 * Response resource describing an IoT device.
 * {@code online} is derived from the last heartbeat; {@code batteryLevel} is a percentage (0-100).
 */
public record DeviceResource(
        Long id,
        Long elderlyProfileId,
        String pairingCode,
        String status,
        Integer batteryLevel,
        Instant lastHeartbeat,
        Instant pairedAt,
        boolean online
) {
}
