package com.famicare.FamicareBackend.zones.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request resource used to define a circular safe zone for an elderly profile
 * @param elderlyProfileId elderly profile the zone belongs to
 * @param name             name of the zone, unique per elderly profile
 * @param latitude         latitude of the center
 * @param longitude        longitude of the center
 * @param radiusMeters     radius of the zone in meters
 */
@Schema(description = "Request resource used to define a circular safe zone for an elderly profile")
public record CreateSafeZoneResource(
        @Schema(
                description = "Identifier of the elderly profile the zone belongs to",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{zone.error.elderlyProfileId.invalid}")
        @Positive(message = "{zone.error.elderlyProfileId.invalid}")
        Long elderlyProfileId,
        @Schema(
                description = "Name of the zone, unique per elderly profile",
                example = "Casa",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{zone.error.name.notBlank}")
        @Size(max = 100, message = "{zone.error.name.size}")
        String name,
        @Schema(
                description = "Latitude of the center in decimal degrees",
                example = "-12.046374",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{coordinates.error.latitude.invalid}")
        @DecimalMin(value = "-90", message = "{coordinates.error.latitude.invalid}")
        @DecimalMax(value = "90", message = "{coordinates.error.latitude.invalid}")
        BigDecimal latitude,
        @Schema(
                description = "Longitude of the center in decimal degrees",
                example = "-77.042793",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{coordinates.error.longitude.invalid}")
        @DecimalMin(value = "-180", message = "{coordinates.error.longitude.invalid}")
        @DecimalMax(value = "180", message = "{coordinates.error.longitude.invalid}")
        BigDecimal longitude,
        @Schema(
                description = "Radius of the zone in meters",
                example = "150",
                minimum = "50",
                maximum = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{zone.error.radius.range}")
        @Min(value = 50, message = "{zone.error.radius.range}")
        @Max(value = 10000, message = "{zone.error.radius.range}")
        Integer radiusMeters
) {
}
