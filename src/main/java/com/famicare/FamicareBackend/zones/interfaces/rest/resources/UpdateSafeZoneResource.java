package com.famicare.FamicareBackend.zones.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request resource used to replace the name and the area of a safe zone
 * @param name         new name of the zone
 * @param latitude     latitude of the new center
 * @param longitude    longitude of the new center
 * @param radiusMeters new radius of the zone in meters
 */
@Schema(description = "Request resource used to replace the name and the area of a safe zone")
public record UpdateSafeZoneResource(
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
