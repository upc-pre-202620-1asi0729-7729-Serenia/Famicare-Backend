package com.famicare.FamicareBackend.iot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request resource used to link an IoT device to an elderly profile
 * @param pairingCode      code printed on the device
 * @param elderlyProfileId elderly profile that will own the device
 */
@Schema(description = "Request resource used to link an IoT device to an elderly profile")
public record PairDeviceResource(
        @Schema(
                description = "Pairing code printed on the device",
                example = "FAMI-7K2P-9XQ4",
                maxLength = 32,
                pattern = "^[A-Za-z0-9-]+$",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{device.error.pairingCode.notBlank}")
        @Size(max = 32, message = "{device.error.pairingCode.size}")
        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "{device.error.pairingCode.pattern}")
        String pairingCode,
        @Schema(
                description = "Identifier of the elderly profile that will own the device",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{device.error.elderlyProfileId.invalid}")
        @Positive(message = "{device.error.elderlyProfileId.invalid}")
        Long elderlyProfileId
) {
}
