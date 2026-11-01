package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

/**
 * Value object referencing an elderly profile owned by the Care bounded context.
 * The IoT context only keeps the identifier, never the Care aggregate itself.
 *
 * @param value elderly profile identifier
 */
public record ElderlyProfileId(Long value) {
    private static final String INVALID_MESSAGE_KEY = "device.error.elderlyProfileId.invalid";

    public ElderlyProfileId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(INVALID_MESSAGE_KEY);
        }
    }
}
