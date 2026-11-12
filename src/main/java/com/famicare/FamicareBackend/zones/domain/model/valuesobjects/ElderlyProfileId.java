package com.famicare.FamicareBackend.zones.domain.model.valuesobjects;

/**
 * Value object referencing an elderly profile owned by the Care bounded context.
 * The Zones context only keeps the identifier, never the Care aggregate itself.
 *
 * @param value elderly profile identifier
 */
public record ElderlyProfileId(Long value) {
    private static final String INVALID_MESSAGE_KEY = "zone.error.elderlyProfileId.invalid";

    public ElderlyProfileId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(INVALID_MESSAGE_KEY);
        }
    }
}
