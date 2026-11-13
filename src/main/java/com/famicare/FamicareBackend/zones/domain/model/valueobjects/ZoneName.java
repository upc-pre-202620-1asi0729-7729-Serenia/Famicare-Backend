package com.famicare.FamicareBackend.zones.domain.model.valueobjects;

/**
 * Value object representing the name a caregiver gives to a safe zone (for example "Casa").
 * The value is trimmed. Names are unique per elderly profile.
 *
 * @param value raw zone name
 */
public record ZoneName(String value) {
    private static final int MAX_LENGTH = 100;
    private static final String NOT_BLANK_MESSAGE_KEY = "zone.error.name.notBlank";
    private static final String SIZE_MESSAGE_KEY = "zone.error.name.size";

    public ZoneName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_MESSAGE_KEY);
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(SIZE_MESSAGE_KEY);
        }
    }
}
