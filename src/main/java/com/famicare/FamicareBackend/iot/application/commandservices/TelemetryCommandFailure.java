package com.famicare.FamicareBackend.iot.application.commandservices;

/**
 * Failure types for telemetry command execution
 */
public sealed interface TelemetryCommandFailure permits
        TelemetryCommandFailure.DeviceNotFound,
        TelemetryCommandFailure.DeviceNotPaired {
    /**
     * @return message key associated with the failure
     */
    String messageKey();

    /** The device that sent the telemetry does not exist. */
    record DeviceNotFound() implements TelemetryCommandFailure {
        @Override
        public String messageKey() { return "device.error.notFound"; }
    }

    /** The device exists but is not linked to an elderly profile, so its telemetry is rejected. */
    record DeviceNotPaired() implements TelemetryCommandFailure {
        @Override
        public String messageKey() { return "device.error.notPaired"; }
    }
}
