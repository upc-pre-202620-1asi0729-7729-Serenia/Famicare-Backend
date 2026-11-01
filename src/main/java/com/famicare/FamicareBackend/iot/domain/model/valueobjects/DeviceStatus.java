package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

/**
 * Lifecycle status of an IoT device (column {@code status} of {@code iot_devices}).
 * Whether the device is online is not stored: it is derived from its last heartbeat.
 */
public enum DeviceStatus {
    /** Registered with a pairing code but not yet linked to an elderly profile. */
    PENDING,
    /** Linked to an elderly profile and allowed to send telemetry. */
    PAIRED
}
