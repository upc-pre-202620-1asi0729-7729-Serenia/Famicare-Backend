package com.famicare.FamicareBackend.iot.domain.model.commands;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;

/**
 * Links an IoT device to an elderly profile using the device's pairing code (US-07).
 *
 * @param pairingCode      code printed on the device
 * @param elderlyProfileId elderly profile that will own the device
 */
public record PairDeviceCommand(PairingCode pairingCode, ElderlyProfileId elderlyProfileId) {

    public PairDeviceCommand {
        if (pairingCode == null)
            throw new IllegalArgumentException("pairingCode cannot be null");
        if (elderlyProfileId == null)
            throw new IllegalArgumentException("elderlyProfileId cannot be null");
    }
}
