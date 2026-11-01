package com.famicare.FamicareBackend.iot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Request resource sent by an IoT device to report telemetry.
 * Which optional fields are required depends on the event type:
 * LOCATION, PANIC and FALL require coordinates; HEARTBEAT requires the battery level.
 */
@Schema(description = "Request resource sent by an IoT device to report telemetry")
public record RecordTelemetryResource(
        @Schema(
                description = "Kind of message",
                example = "LOCATION",
                allowableValues = {"LOCATION", "HEARTBEAT", "PANIC", "FALL"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{telemetry.error.eventType.invalid}")
        String eventType,
        @Schema(description = "Latitude in decimal degrees", example = "-12.046374")
        @DecimalMin(value = "-90", message = "{coordinates.error.latitude.invalid}")
        @DecimalMax(value = "90", message = "{coordinates.error.latitude.invalid}")
        BigDecimal latitude,
        @Schema(description = "Longitude in decimal degrees", example = "-77.042793")
        @DecimalMin(value = "-180", message = "{coordinates.error.longitude.invalid}")
        @DecimalMax(value = "180", message = "{coordinates.error.longitude.invalid}")
        BigDecimal longitude,
        @Schema(description = "Battery percentage", example = "85", minimum = "0", maximum = "100")
        @Min(value = 0, message = "{device.error.batteryLevel.range}")
        @Max(value = 100, message = "{device.error.batteryLevel.range}")
        Integer batteryLevel,
        @Schema(
                description = "Instant at which the device produced the reading (ISO-8601, UTC)",
                example = "2026-10-31T23:10:00Z",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{telemetry.error.recordedAt.required}")
        Instant recordedAt
) {
}
