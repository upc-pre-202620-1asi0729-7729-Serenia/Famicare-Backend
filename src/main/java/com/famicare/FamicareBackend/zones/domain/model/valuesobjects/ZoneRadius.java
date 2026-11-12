package com.famicare.FamicareBackend.zones.domain.model.valuesobjects;

/**
 * Value object representing the radius, in meters, of a circular safe zone.
 * The lower bound keeps the zone larger than the usual GPS error so that a still
 * person does not cross its boundary because of noise alone.
 *
 * @param value radius in meters
 */
public record ZoneRadius(int value) {
    public static final int MIN_METERS = 50;
    public static final int MAX_METERS = 10_000;
    private static final String RANGE_MESSAGE_KEY = "zone.error.radius.range";

    public ZoneRadius {
        if (value < MIN_METERS || value > MAX_METERS) {
            throw new IllegalArgumentException(RANGE_MESSAGE_KEY);
        }
    }
}
