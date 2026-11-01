package com.famicare.FamicareBackend.iot.application.commandservices;

/**
 * Failure types for device command execution
 */
public sealed interface DeviceCommandFailure permits
        DeviceCommandFailure.DuplicatePairingCode,
        DeviceCommandFailure.PairingCodeNotFound,
        DeviceCommandFailure.AlreadyPaired {
    /**
     * @return message key associated with the failure
     */
    String messageKey();

    /** A device with the same pairing code is already registered. */
    record DuplicatePairingCode() implements DeviceCommandFailure {
        @Override
        public String messageKey() { return "device.error.pairingCode.duplicate"; }
    }

    /** No registered device matches the pairing code. */
    record PairingCodeNotFound() implements DeviceCommandFailure {
        @Override
        public String messageKey() { return "device.error.pairingCode.notFound"; }
    }

    /** The device is already linked to an elderly profile. */
    record AlreadyPaired() implements DeviceCommandFailure {
        @Override
        public String messageKey() { return "device.error.alreadyPaired"; }
    }
}
