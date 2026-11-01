package com.famicare.FamicareBackend.iot.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request resource used to register a new IoT device
 * @param pairingCode code printed on the device
 */
@Schema(description = "Request resource used to register a new IoT device")
public record RegisterDeviceResource(
        @Schema(
                description = "Pairing code printed on the device",
                example = "FAMI-7K2P-9XQ4",
                maxLength = 32,
                pattern = "^[A-Za-z0-9-]+$",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{device.error.pairingCode.notBlank}")
        @Size(max = 32, message = "{device.error.pairingCode.size}")
        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "{device.error.pairingCode.pattern}")
        String pairingCode
) {
}
