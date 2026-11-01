package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Value object representing the pairing code printed on an IoT device.
 * The caregiver administrator enters it to link the device to an elderly profile.
 * The value is trimmed and normalized to upper case so that the match is case-insensitive.
 *
 * @param value raw pairing code
 */
public record PairingCode(String value) {
    private static final int MAX_LENGTH = 32;
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[A-Z0-9-]+$");
    private static final String NOT_BLANK_MESSAGE_KEY = "device.error.pairingCode.notBlank";
    private static final String SIZE_MESSAGE_KEY = "device.error.pairingCode.size";
    private static final String PATTERN_MESSAGE_KEY = "device.error.pairingCode.pattern";

    public PairingCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_MESSAGE_KEY);
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(SIZE_MESSAGE_KEY);
        }
        if (!ALLOWED_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(PATTERN_MESSAGE_KEY);
        }
    }

    public String maskedForLogging() {
        if (value.length() <= 4) return "****";
        return "****" + value.substring(value.length() - 4);
    }
}
