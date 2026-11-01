package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.TelemetryEventType;

import java.util.Locale;

public class IotValueObjectFromStringAssembler {

    public static PairingCode toPairingCodeFromString(String value) {
        return new PairingCode(value);
    }

    /**
     * @param value event type name, case-insensitive
     * @return matching {@link TelemetryEventType}
     * @throws IllegalArgumentException with message key {@code telemetry.error.eventType.invalid} when unknown
     */
    public static TelemetryEventType toTelemetryEventTypeFromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("telemetry.error.eventType.invalid");
        }
        try {
            return TelemetryEventType.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("telemetry.error.eventType.invalid");
        }
    }
}
