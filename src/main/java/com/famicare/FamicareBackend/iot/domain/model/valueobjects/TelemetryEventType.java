package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

/**
 * Kind of message an IoT device can send (column {@code event_type} of {@code telemetry_events}).
 */
public enum TelemetryEventType {
    /** Periodic GPS position. */
    LOCATION(true, false),
    /** Periodic "I am alive" signal carrying the battery level. */
    HEARTBEAT(false, true),
    /** Panic button pressed by the elderly person; carries the current position. */
    PANIC(true, false),
    /** Fall detected by the device, when the hardware supports it. */
    FALL(true, false);

    private final boolean requiresCoordinates;
    private final boolean requiresBatteryLevel;

    TelemetryEventType(boolean requiresCoordinates, boolean requiresBatteryLevel) {
        this.requiresCoordinates = requiresCoordinates;
        this.requiresBatteryLevel = requiresBatteryLevel;
    }

    public boolean requiresCoordinates() {
        return requiresCoordinates;
    }

    public boolean requiresBatteryLevel() {
        return requiresBatteryLevel;
    }
}
