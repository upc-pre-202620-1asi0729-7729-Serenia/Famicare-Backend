package com.famicare.FamicareBackend.iot.domain.model.commands;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;

/**
 * Registers a new IoT device (stock/provisioning) identified by its pairing code.
 *
 * @param pairingCode code printed on the device
 */
public record RegisterDeviceCommand(PairingCode pairingCode) {

    public RegisterDeviceCommand {
        if (pairingCode == null)
            throw new IllegalArgumentException("pairingCode cannot be null");
    }
}
