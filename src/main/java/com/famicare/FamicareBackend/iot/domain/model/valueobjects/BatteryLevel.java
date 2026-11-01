package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

/**
 * Value object representing the battery percentage reported by an IoT device (0 to 100).
 *
 * @param value battery percentage
 */
public record BatteryLevel(int value) {
    private static final int MIN = 0;
    private static final int MAX = 100;
    private static final String RANGE_MESSAGE_KEY = "device.error.batteryLevel.range";

    public BatteryLevel {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException(RANGE_MESSAGE_KEY);
        }
    }

    /**
     * @param threshold percentage below which the battery is considered low
     * @return {@code true} when the level is strictly below the threshold
     */
    public boolean isBelow(int threshold) {
        return value < threshold;
    }
}
